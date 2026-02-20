console.log("Email Writer Loaded");

function getEmailContent(){
    const selectors=['.h7','.a3s.aiL','.gmail_qoute','[role="presentation"]'];
    for(const selector of selectors){
        const content=document.querySelector(selector);
        if(content){
            return content.innerText.trim();
        }
    }
    return '';
}

function finnComposeToolbar(){
    const selectors=['.btC','.aDh', '[role="toolbar"]','.gU.Up'];
    for(const selector of selectors){
        const toolbar=document.querySelector(selector);
        if(toolbar){
            return toolbar;
        }
    }
    return null;
}

function createAIButton(){
    const button=document.createElement('div');
    button.className='T-I J-J5-Ji aoO v7 T-I-atl L3';
    button.style.marginRight='8px';
    button.innerHTML='AI Reply'
    button.setAttribute('role','button');
    button.setAttribute('data-tooltip','Generate AI Reply');
    return button;
}

function injectButton(){
    const existingButton=document.querySelector('.ai_reply_button');
    if(existingButton){
        existingButton.remove();
    }

    const toolbar=finnComposeToolbar();
    if(!toolbar){
        console.log("Toolbar not found");
        return;
    }

    console.log("Toolbar found");
    const button=createAIButton();
    button.classList.add('ai_reply_button');

    button.addEventListener('click', async () => {
        try {
            button.innerHTML='Generating...';
            button.disabled=true;
            const emailContent=getEmailContent();

            const response = await fetch('http://localhost:8081/api/email/generate',{
                method: 'POST', 
                headers:{
                    'Content-Type':'application/json',
                },
                body:JSON.stringify({
                    emailContent:emailContent,
                    tone:"professional"
                })
            });
            if(!response.ok){
                throw new Error("API Request Failed with status: " + response.status);
            }
            const generatedReply = await response.text();
            const composeBox = document.querySelector(
                '[role="textbox"][g_editable="true"]'
            );
            if(composeBox){
                composeBox.focus();
                document.execCommand('insertText', false, generatedReply);
            }
            button.innerHTML = 'AI Reply';
            button.disabled = false;

        } catch (error) {
        }
    });
   
    toolbar.insertBefore(button,toolbar.firstChild);
}
const observer = new MutationObserver((mutationRecords) => {
    for (const record of mutationRecords) {
        const addedNodes = Array.from(record.addedNodes);

        const hasComposeElements = addedNodes.some(node =>
            node.nodeType === Node.ELEMENT_NODE &&
            (
                node.matches('.aDh, .btC, [role="dialog"]') ||
                node.querySelector('.aDh, .btC, [role="dialog"]')
            )
        );
        if (hasComposeElements) {
            console.log("Compose Window Detected");
            setTimeout(injectButton,500);
        }
    }
});
observer.observe(document.body, {
    childList: true,
    subtree: true
});