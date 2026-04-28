const container = document.getElementById("authorsContainer");
const statusEl = document.getElementById("status");

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

function normalizeAuthor(a) {
    return { fullName: a.fullName ?? a.fullName?.() ?? a.name ?? "" };
}

function renderRow(author) {
    return `
    <div style="display: flex; flex-direction: row; align-items: center; gap: 20px; padding: 10px; background-color: bisque; border-bottom: lightgray 1px solid">
      <div style="flex-grow: 1; flex-basis: 300px">${escapeHtml(author.fullName)}</div>
    </div>
  `;
}

async function loadAuthors() {
    setStatus("Loading...");
    try {
        const raw = await api("api/authors");
        const authors = (raw || []).map(normalizeAuthor);

        container.innerHTML = authors.map(renderRow).join("");
        setStatus("");
    } catch (e) {
        setStatus("Load error: " + e.message);
    }
}

loadAuthors();
