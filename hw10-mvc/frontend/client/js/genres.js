const container = document.getElementById("genresContainer");
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

// поддержка разных DTO:
// { name: "..." } или { name(): "..." } или просто строка "Fantasy" (если так отдашь)
function normalizeGenre(g) {
    if (typeof g === "string") return { name: g };
    return { name: g.name ?? g.name?.() ?? "" };
}

function renderRow(genre) {
    return `
    <div style="display: flex; flex-direction: row; align-items: center; gap: 20px; padding: 10px; background-color: bisque; border-bottom: lightgray 1px solid">
      <div style="flex-grow: 1; flex-basis: 300px">${escapeHtml(genre.name)}</div>
    </div>
  `;
}

async function loadGenres() {
    setStatus("Loading...");
    try {
        // ожидаем: GET /api/genres -> [{name:"..."}]
        const raw = await api("/api/genre");
        const genres = (raw || []).map(normalizeGenre);

        container.innerHTML = genres.map(renderRow).join("");
        setStatus("");
    } catch (e) {
        setStatus("Load error: " + e.message);
    }
}

loadGenres();
