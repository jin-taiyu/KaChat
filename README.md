# KaChat - Real-time Chat Application

KaChat is a modern, real-time chat application built with Vue.js for the frontend and Kotlin with Ktor for the backend. It provides an online chatting experience with features like private messaging, public chat rooms, and more.

## ✨ Features

- **Real-time Messaging**: Instant message delivery with WebSocket support
- **Private & Public Chats**: Switch between private conversations and public chat rooms
- **Contact List**: Manage and view all available contacts
- **Message History**: View past conversations with any user

## 🚀 Getting Started

### Prerequisites

- Docker and Docker Compose installed
- Node.js (v18+)
- Java 17

### Installation

1. Clone the repository:
   ```bash
   https://github.com/jin-taiyu/KaChat.git
   cd KaChat
   ```

2. Start the application using Docker Compose:
   ```bash
   docker compose up --build
   ```

3. Access the application:
   - Frontend: http://localhost:3000
   - Backend: http://localhost:8080

### Development Setup

#### Frontend
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

#### Backend
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Run the application:
   ```bash
   ./gradlew run
   ```

## 🛠️ Tech Stack

### Frontend
- Vue.js 3
- Vue Router
- Axios
- Vite
- Tailwind CSS (optional)

### Backend
- Kotlin
- Ktor
- Exposed ORM
- PostgreSQL
- JWT Authentication
- WebSockets

### Infrastructure
- Docker
- Docker Compose
- Nginx
- PostgreSQL
