# AI Email Assistant - Chrome Extension

Welcome to the **AI Email Assistant** project! This is a full-stack application that integrates seamlessly into Gmail to help users generate professional, AI-powered email replies with just one click. 

This project is divided into two main parts:
1. **Frontend:** A Chrome Extension built with JavaScript, HTML, and CSS.
2. **Backend:** A RESTful API built with Java and Spring Boot.

---

## How It Works (The Flow)
1. **User opens Gmail:** The Chrome extension detects the compose window.
2. **Extracts Content:** The extension reads the original email you want to reply to.
3. **API Call:** It sends this email content to our local Spring Boot backend.
4. **AI Generation:** The backend processes the prompt and calls an AI model (like Gemini/OpenAI) to generate a context-aware, professional reply.
5. **Auto-Fill:** The extension receives the reply and automatically types it into the Gmail compose box.

---

## 🧩 1. Backend: Java Spring Boot (`AI-Email-Assistant-Backend`)

### Why Java Spring Boot?
Spring Boot is an industry-standard, robust framework. We used it here because:
* It makes creating REST APIs incredibly fast and secure.
* It handles Cross-Origin Resource Sharing (CORS) easily (crucial for Chrome Extensions).
* It provides `WebClient` for efficient, non-blocking calls to external AI APIs.

### 📂 Backend File Structure & What They Do:
* `src/main/java/com/email/writer/`
  * `EmailWriterSbApplication.java`: The main entry point that starts the Spring Boot server.
  * `EmailGeneratorController.java`: The API gateway. It receives the HTTP `POST` requests from our Chrome extension.
  * `EmailGeneratorService.java`: The brain of the backend. It contains the logic to format the prompt and communicate with the external AI API.
  * `EmailRequest.java`: A DTO (Data Transfer Object) class that defines the structure of the incoming JSON data (email content and tone).
  * `WebClientConfig.java`: Configuration file to set up the HTTP client used to call the AI model.
* `pom.xml`: Manages all the project dependencies (like Spring Web, WebFlux, etc.).

### 🛠️ How to Test the Backend using Postman
Before connecting the extension, you can test if the backend is working correctly:
1. Start your Spring Boot application (it usually runs on `http://localhost:8081`).
2. Open Postman and create a new **POST** request.
3. Set the URL to: `http://localhost:8081/api/email/generate`
4. Go to the **Body** tab, select **raw**, and choose **JSON** from the dropdown.
5. Paste this JSON:
   ```json
   {
       "emailContent": "Hey, how is the startup going? Let's catch up soon.",
       "tone": "professional"
   }

## 🧩 2. Frontend: Chrome Extension (`AI-Email-Extension`)

### 📂 Frontend File Structure & What They Do:
* **`manifest.json`**: The most important file! It tells Chrome what the extension is, what permissions it needs, and which scripts to run.
* **`content.js`**: The script that is injected directly into Gmail. It creates the "AI Reply" button, reads the email text, makes the `fetch` call, and types the result.
* **`content.css`**: Contains the styling for our custom "AI Reply" button.
* **`hello.html`**: The UI for the extension's popup menu.

### 🛠️ How to Install and Test the Extension
1. Open Google Chrome and go to `chrome://extensions/`.
2. Turn ON **Developer mode** (top right corner).
3. Click on **Load unpacked** in the top left.
4. Select the `AI-Email-Extension` folder.
5. Ensure your Spring Boot backend is running.
6. Open Gmail, hit "Reply" on an email, and click the new **AI Reply** button!

---

### Here is snapshot of UI
![Project Demo](https://github.com/AgrimChauhan09/AI-Email-Assistant/blob/main/Demo/extension.png?raw=true)
![Project Demo](https://github.com/AgrimChauhan09/AI-Email-Assistant/blob/main/Demo/MailUi.png?raw=true)


## 🤝 Feedback & Support
I built this project to streamline email communications using AI and to explore the integration of browser extensions with robust Java backends.

If you have any suggestions to make this tool better, find any bugs, or just want to discuss the code, feel free to reach out to me! I am always open to feedback and improvements.

📧 **Contact me at:** [agrimchauhan.18@gmail.com](mailto:agrimchauhan.18@gmail.com)
