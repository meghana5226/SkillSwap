/**
 * Deliberately minimal Markdown renderer — AI responses use headers, bold
 * text, and lists, and that's it. Pulling in a full Markdown library for
 * this would be overkill; this covers what the prompts actually produce.
 */
function renderInline(text: string): string {
  return text
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/\*\*(.+?)\*\*/g, "<strong>$1</strong>")
    .replace(/\*(.+?)\*/g, "<em>$1</em>")
    .replace(/`(.+?)`/g, "<code class='rounded bg-slate-200 px-1 py-0.5 text-[0.85em] dark:bg-slate-800'>$1</code>");
}

export function MarkdownLite({ content }: { content: string }) {
  const lines = content.split("\n");
  const blocks: string[] = [];
  let listBuffer: string[] = [];

  function flushList() {
    if (listBuffer.length > 0) {
      blocks.push(`<ul class="list-disc pl-5 space-y-1">${listBuffer.join("")}</ul>`);
      listBuffer = [];
    }
  }

  for (const rawLine of lines) {
    const line = rawLine.trim();

    if (line.startsWith("### ")) {
      flushList();
      blocks.push(`<h4 class="font-semibold mt-3">${renderInline(line.slice(4))}</h4>`);
    } else if (line.startsWith("## ")) {
      flushList();
      blocks.push(`<h3 class="font-semibold text-lg mt-4">${renderInline(line.slice(3))}</h3>`);
    } else if (line.startsWith("# ")) {
      flushList();
      blocks.push(`<h2 class="font-bold text-xl mt-4">${renderInline(line.slice(2))}</h2>`);
    } else if (/^[-*]\s+/.test(line)) {
      listBuffer.push(`<li>${renderInline(line.replace(/^[-*]\s+/, ""))}</li>`);
    } else if (line === "") {
      flushList();
    } else {
      flushList();
      blocks.push(`<p class="mt-2 leading-relaxed">${renderInline(line)}</p>`);
    }
  }
  flushList();

  return (
    <div
      className="text-sm text-slate-700 dark:text-slate-300"
      dangerouslySetInnerHTML={{ __html: blocks.join("") }}
    />
  );
}
