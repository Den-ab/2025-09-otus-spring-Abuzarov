const form = document.getElementById("editBookForm");
const bookIdEl = document.getElementById("bookId");
const titleEl = document.getElementById("title");
const genreEl = document.getElementById("genreId");
const authorEl = document.getElementById("authorId");
const commentsEl = document.getElementById("commentsContainer");
const statusEl = document.getElementById("status");
const saveBtn = document.getElementById("saveBtn");

function setStatus(text) {
    statusEl.textContent = text || "";
}

function escapeHtml(s) {
    return String(s ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

async function api(url, opts = {}) {
    const res = await fetch(url, {
        headers: { "Content-Type": "application/json", ...(opts.headers || {}) },
        ...opts
    });

    if (!res.ok) {
        const text = await res.text().catch(() => "");
        throw new Error(text || `HTTP ${res.status}`);
    }

    const ct = res.headers.get("content-type") || "";
    return ct.includes("application/json") ? res.json() : null;
}

function getBookIdFromUrl() {
    const parts = window.location.pathname.split("/").filter(Boolean);
    const idx = parts.indexOf("books");
    if (idx === -1 || !parts[idx + 1]) return null;
    return parts[idx + 1];
}

function normalizeGenre(g) {
    return { id: g.id ?? g.id?.(), name: g.name ?? g.name?.() ?? "" };
}

function normalizeAuthor(a) {
    return { id: a.id ?? a.id?.(), name: a.fullName ?? a.fullName?.() ?? a.name ?? "" };
}

function normalizeBook(b) {
    const id = b.id ?? b.id?.();
    const title = b.title ?? b.title?.() ?? "";
    const genreId = b.genre?.id ?? b.genre?.id?.() ?? b.genreId ?? "";
    const authorId = b.author?.id ?? b.author?.id?.() ?? b.authorId ?? "";
    const comments = b.comments ?? [];
    return { id, title, genreId, authorId, comments };
}

function normalizeComment(c) {
    return { content: c.content ?? c.content?.() ?? String(c) };
}

function fillSelect(selectEl, items, placeholder, selectedId) {
    selectEl.innerHTML = `
    <option value="">${escapeHtml(placeholder)}</option>
    ${items.map(i => {
        const sel = String(i.id) === String(selectedId) ? "selected" : "";
        return `<option value="${escapeHtml(i.id)}" ${sel}>${escapeHtml(i.name)}</option>`;
    }).join("")}
  `;
}

function renderComments(comments) {
    if (!comments || comments.length === 0) {
        commentsEl.innerHTML = "";
        return;
    }
    commentsEl.innerHTML = comments.map(c => `
    <div style="background-color: lightgray; padding: 10px; font-size: 14px; margin-bottom: 40px;">
      ${escapeHtml(c.content)}
    </div>
  `).join("");
}

async function loadPage() {
    const id = getBookIdFromUrl();
    if (!id) {
        setStatus("Cannot detect book id in URL");
        return;
    }

    setStatus("Loading...");
    try {
        const bookRaw = await api(`/api/book/${encodeURIComponent(id)}`);
        const book = normalizeBook(bookRaw);

        bookIdEl.value = book.id;
        titleEl.value = book.title;

        const [genresRaw, authorsRaw, commentsRaw] = await Promise.all([
            api("/api/genre"),
            api("/api/author"),
            api(`/api/book/${encodeURIComponent(id)}/comments`).catch(() => null)
        ]);

        const genres = (genresRaw || []).map(normalizeGenre);
        const authors = (authorsRaw || []).map(normalizeAuthor);

        fillSelect(genreEl, genres, "Select genre", book.genreId);
        fillSelect(authorEl, authors, "Select author", book.authorId);

        const comments = (commentsRaw ? commentsRaw : book.comments).map(normalizeComment);
        renderComments(comments);

        setStatus("");
    } catch (e) {
        setStatus("Load error: " + e.message);
    }
}

function validate() {
    const title = titleEl.value.trim();
    const genreId = genreEl.value;
    const authorId = authorEl.value;

    if (!title) return "Title is required";
    if (!genreId) return "Genre is required";
    if (!authorId) return "Author is required";
    return null;
}

async function saveChanges() {
    const err = validate();
    if (err) {
        setStatus(err);
        return;
    }

    const id = bookIdEl.value || getBookIdFromUrl();
    if (!id) {
        setStatus("No book id");
        return;
    }

    const payload = {
        title: titleEl.value.trim(),
        genreId: genreEl.value,
        authorId: authorEl.value
    };

    saveBtn.disabled = true;
    setStatus("Saving...");

    try {
        await api(`/api/book/${encodeURIComponent(id)}`, {
            method: "PUT",
            body: JSON.stringify(payload)
        });

        setStatus("");
        window.location.href = "/";
    } catch (e) {
        setStatus("Save error: " + e.message);
    } finally {
        saveBtn.disabled = false;
    }
}

form.addEventListener("submit", (e) => {
    e.preventDefault();
    saveChanges();
});

loadPage();
