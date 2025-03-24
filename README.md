# 🤖 TelegramNutritionAiBot · AI-Powered Telegram Bot with Spring AI

**TelegramNutritionAiBot** is a modular, pluggable and production-ready **AI-powered nutrition assistant** built with *
*Java 21**, **Spring Boot 3.2+**, and **Spring AI** — the new abstraction for integrating **multiple AI providers** in
your applications.

> 🔥 Powered by [**Spring AI**](https://docs.spring.io/spring-ai/reference/) — easily switch between OpenAI, Azure
> OpenAI, Mistral, Ollama, HuggingFace, and more!

---

## 💡 What This Bot Can Do

### 🧠 AI-Powered Features

- 📊 **Generate Nutrition Plan (GPT-4/any AI)**
    - AI creates a structured daily plan from user input (age, weight, goals)
    - Supports JSON-based structured output with Spring AI

- 📷 **Food Photo Recognition**
    - Upload food photos and get KBZhU (calories, proteins, fats, carbs)

- ✍️ **Text-Based Food Entry**
    - Describe your food and get instant nutrition insights

- 📑 **Daily Report**
    - Summarizes total food intake vs. nutrition plan

- 🌍 **Multilingual Support**
    - Supports Russian 🇷🇺 and English 🇺🇸 via externalized translations and prompts

---

## 🧠 Why Spring AI?

This project uses **Spring AI** to abstract away the actual AI vendor:

### 🔌 Plug-and-play any AI model:

| Provider       | Supported? |
|----------------|------------|
| OpenAI (GPT-4) | ✅ Default  |
| Azure OpenAI   | ✅          |
| HuggingFace    | ✅          |
| Mistral        | ✅          |
| Ollama (local) | ✅          |
| AWS Bedrock    | ✅          |
| Groq           | ✅          |
| Google Gemini  | ✅ (soon)   |

You can **easily swap providers** by changing a few Spring config lines. No need to rewrite logic — thanks to Spring
AI’s `ChatClient` abstraction.

---

## 💬 Telegram Commands

| Command   | Description                      |
|-----------|----------------------------------|
| `/start`  | Start bot & show options         |
| `/new`    | Input personal parameters        |
| `/reset`  | Clear saved profile & plan       |
| `/report` | Show today's food intake summary |
| `/stop`   | Stop tracking                    |
