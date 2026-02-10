import express from "express";
import path from "path";
import { fileURLToPath } from "url";
import { createProxyMiddleware } from "http-proxy-middleware";

const app = express();

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const clientDir = path.join(__dirname, "client");

// Прокси API на Spring
app.use("/api", createProxyMiddleware({
    target: "http://localhost:8080",
    changeOrigin: true
}));

// Раздача статики
app.use(express.static(clientDir));

app.get("/", (_, res) => res.sendFile(path.join(clientDir, "index.html")));
app.get("/authors", (_, res) => res.sendFile(path.join(clientDir, "authors.html")));
app.get("/genres", (_, res) => res.sendFile(path.join(clientDir, "genres.html")));
app.get("/books/new", (_, res) => res.sendFile(path.join(clientDir, "book-new.html")));
app.get("/books/:id", (_, res) => res.sendFile(path.join(clientDir, "book-edit.html")));

app.listen(3000, () => {
    console.log("Client running: http://localhost:3000");
});
