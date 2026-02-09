const container = document.getElementById("booksContainer");
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

// НОРМ путь под твою модель, судя по thymeleaf:
// book.genre().name(), book.title(), book.author().fullName()
// но в REST это может быть genre.name / author.fullName — я поддержал оба варианта
function normalizeBook(b) {
    const id = b.id ?? b.id?.() ?? "";
    const title = b.title ?? b.title?.() ?? "";
    const genreName =
        b.genre?.name ?? b.genre?.name?.() ?? b.genreName ?? "";
    const authorName =
        b.author?.fullName ?? b.author?.fullName?.() ?? b.authorName ?? "";

    return { id, title, genreName, authorName };
}

function renderRow(book) {
    const id = escapeHtml(book.id);

    // div вместо <a> (иначе delete-button внутри ссылки = боль)
    return `
    <div class="book-row"
         data-id="${id}"
         style="cursor:pointer; text-decoration:none; color:black">
      <div style="display: flex; flex-direction: row; align-items: center; gap: 20px; padding: 10px; background-color: bisque; border-bottom: lightgray 1px solid">
        <div style="flex-basis: 200px">${escapeHtml(book.genreName)}</div>
        <div style="flex-grow: 1; flex-basis: 200px">${escapeHtml(book.title)}</div>
        <div style="flex-basis: 300px">${escapeHtml(book.authorName)}</div>
        <div style="flex-basis: 200px">
          <button class="delete-btn"
                  data-action="delete"
                  style="cursor:pointer; width:80px; height:30px; background-color: purple; color: white; border:none;">
            Delete
          </button>
        </div>
      </div>
    </div>
  `;
}

async function loadBooks() {
    setStatus("Loading...");
    try {
        // ожидаем: GET /api/books -> [{id, title, genre:{name}, author:{fullName}}]
        const raw = await api("/api/book");
        const books = (raw || []).map(normalizeBook);

        container.innerHTML = books.map(renderRow).join("");
        setStatus("");
    } catch (e) {
        setStatus("Load error: " + e.message);
    }
}

async function deleteBook(id) {
    setStatus("Deleting...");
    try {
        await api(`/api/book/${encodeURIComponent(id)}`, { method: "DELETE" });
        await loadBooks();
        setStatus("");
    } catch (e) {
        setStatus("Delete error: " + e.message);
    }
}

// Делегирование кликов: строка открывает, кнопка удаляет
container.addEventListener("click", (e) => {
    const deleteBtn = e.target.closest(".delete-btn");
    const row = e.target.closest(".book-row");
    if (!row) return;

    const id = row.getAttribute("data-id");

    if (deleteBtn) {
        e.preventDefault();
        e.stopPropagation();
        deleteBook(id);
        return;
    }

    // переход на страницу книги (пока оставил как было)
    window.location.href = `/books/${encodeURIComponent(id)}`;
});

loadBooks();
