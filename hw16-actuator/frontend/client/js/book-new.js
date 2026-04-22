const form = document.getElementById("createBookForm");
const titleEl = document.getElementById("title");
const genreEl = document.getElementById("genreId");
const authorEl = document.getElementById("authorId");
const statusEl = document.getElementById("status");
const createBtn = document.getElementById("createBtn");

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

function normalizeGenre(g) {
    return {
        id: g.id ?? g.id?.(),
        name: g.name ?? g.name?.()
    };
}

function normalizeAuthor(a) {
    return {
        id: a.id ?? a.id?.(),
        fullName: a.fullName ?? a.fullName?.() ?? a.name ?? ""
    };
}

function fillSelect(selectEl, items, placeholder) {
    selectEl.innerHTML = `
    <option value="">${escapeHtml(placeholder)}</option>
    ${items.map(i => `<option value="${escapeHtml(i.id)}">${escapeHtml(i.name ?? i.fullName)}</option>`).join("")}
  `;
}

async function loadDictionaries() {
    setStatus("Loading dictionaries...");
    try {
        const [genresRaw, authorsRaw] = await Promise.all([
            api("/api/genres"),
            api("/api/authors")
        ]);

        const genres = (genresRaw || []).map(normalizeGenre);
        const authors = (authorsRaw || []).map(normalizeAuthor);

        fillSelect(genreEl, genres, "Select genre");
        fillSelect(authorEl, authors, "Select author");

        setStatus("");
    } catch (e) {
        setStatus("Load error: " + e.message);
        genreEl.innerHTML = `<option value="">(failed to load)</option>`;
        authorEl.innerHTML = `<option value="">(failed to load)</option>`;
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

async function createBook() {
    const err = validate();
    if (err) {
        setStatus(err);
        return;
    }

    const payload = {
        title: titleEl.value.trim(),
        genreId: genreEl.value,
        authorId: authorEl.value
    };

    createBtn.disabled = true;
    setStatus("Creating...");

    try {
        await api("/api/books", {
            method: "POST",
            body: JSON.stringify(payload)
        });

        setStatus("");
        window.location.href = `/`;
    } catch (e) {
        setStatus("Create error: " + e.message);
    } finally {
        createBtn.disabled = false;
    }
}

form.addEventListener("submit", (e) => {
    e.preventDefault();
    createBook();
});

loadDictionaries();
