# Nexus Cloud Share
![image alt](https://github.com/kiran948/Nexus-Cloud-Share/blob/49b760b6c940a4f4c3d59e0462c90b018d853e3b/Screenshot%20(1).png)



---

### 📌 Project Overview

Nexus Cloud Share is a full-stack cloud storage platform that allows users to upload, manage, and securely share files online. The system includes features like private/public file sharing, real-time uploads, user authentication, Razorpay-based payments, and a credit management system.

The frontend is built with React + Vite + Tailwind CSS for a fast, responsive UI, while the backend uses Spring Boot to handle APIs, file storage, JWT validation, credit logic, and secure access.
This project was built to demonstrate real-world skills in full-stack development, API design, authentication, payments, and cloud-based file handling.

---

### 🚀 Key Features

🔹 **File Management**

- Upload, download, and delete files
- Supports PDFs, images, documents, videos, and more
- Real-time upload status and progress bar
- Secure file storage with unique encrypted file paths

🔹 **User Authentication (Clerk + JWT)**

- Secure login & signup
- Clerk-managed JWT tokens
- Role-based API access through Spring Boot filters
- Session validation on every request

🔹 **Secure File Sharing**

- Private Files: Only owner can access
- Public Files: Share via secure URL
- Expiry-based links (optional future enhancement-ready)

🔹 **Payment & Credit System**

- Integrated Razorpay payment gateway
- Users can buy credits to increase storage and unlock features
- Dynamic credit deduction for uploads
- Real-time credit updates on UI

🔹 **Advanced Backend Processing**

- Spring Boot REST API
- MySQL storage for users, files, transactions
- Cloud-style folder/file metadata structure
- Robust exception handling and security filters
- File streaming for efficient downloads

🔹 **Frontend Experience**

- Built with React + Vite for performance
- Modern UI with Tailwind CSS
- Mobile responsive layout
- File preview support
- User dashboard with analytics

---

### 🛠️ Tech Stack

**Frontend**

- React + Vite
- Tailwind CSS
- Axios
- Clerk Authentication
- React Router

**Backend**

- Java
- Spring Boot
- Spring Security
- JWT Validation
- Razorpay API
- MySQL
- Lombok

---

### 🔒 Security Highlights

- JWT token validation using Clerk
- Spring Boot authentication filters
- CORS configured for secure frontend-backend communication
- File access controlled via user ownership
- Razorpay signature verification on payment completion

---

### ⚙️ Prerequisites

**Before setting up Nexus Cloud Share, ensure you have:**

- Node.js 18+
- Java 21+
- Maven
- MySQL Server
- Razorpay Test Keys
- Clerk Project Setup (Frontend Authentication)

### 📁 Project Structure
```
Nexus-Cloud-Share/
│
├── backend/                  # Spring Boot app
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── config/
│   └── util/
│
└── frontend/                 # React + Vite app
    ├── components/
    ├── pages/
    ├── hooks/
    ├── services/
    └── context/
```
### 🔧 Setup & Installation
**Backend Setup (Spring Boot)**

1.Clone the repo
```
git clone https://github.com/kiran948/Nexus-CLoud-Share.git
cd Nexus-Cloud-Share/NexusCloudShare
```

2.Configure application.properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/nexus
spring.datasource.username=root
spring.datasource.password=yourpassword

RAZORPAY_KEY=your_key
RAZORPAY_SECRET=your_secret
```

3.Start backend
```
mvn spring-boot:run
```

**Frontend Setup (React + Vite)**

1.Go to frontend directory
```
cd ../Nexus Cloud Share Frontend
```

2.Install dependencies
```
npm install
```

3.Configure Clerk
```
VITE_CLERK_PUBLISHABLE_KEY=your_clerk_key
VITE_BACKEND_URL=http://localhost:8080
```

4.Run React dev server
```
npm run dev
```
