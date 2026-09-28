import csv
import os
import matplotlib.pyplot as plt

n = [100, 500, 1000, 2000, 3000, 4000, 5000]

CSV_PATH = "results/scaling_results.csv"
OUTPUT_DIR = "charts"

os.makedirs(OUTPUT_DIR, exist_ok=True)


def load_family(family_name):
    heap_ops, heap_ms, list_ops, list_ms = [], [], [], []

    with open(CSV_PATH, newline="") as f:
        reader = csv.DictReader(f)
        rows = {int(row["n"]): row for row in reader if row["family"] == family_name}

    for value in n:
        row = rows[value]
        heap_ops.append(int(row["heapOperations"]))
        heap_ms.append(float(row["heapMs"]))
        list_ops.append(int(row["listOperations"]))
        list_ms.append(float(row["listMs"]))

    return heap_ops, heap_ms, list_ops, list_ms


growing_heap_ops, growing_heap_ms, growing_list_ops, growing_list_ms = load_family("growing")
fixed_heap_ops, fixed_heap_ms, fixed_list_ops, fixed_list_ms = load_family("fixed")

plt.rcParams.update({
    "font.size": 11,
    "axes.spines.top": False,
    "axes.spines.right": False,
    "axes.grid": True,
    "grid.alpha": 0.3,
})

HEAP_COLOR = "#2563eb"
LIST_COLOR = "#dc2626"


def make_chart(filename, title, ylabel, heap_vals, list_vals, heap_label, list_label, log_y=False):
    fig, ax = plt.subplots(figsize=(7, 4.5), dpi=150)
    ax.plot(n, heap_vals, marker="o", color=HEAP_COLOR, linewidth=2, label=heap_label)
    ax.plot(n, list_vals, marker="o", color=LIST_COLOR, linewidth=2, label=list_label)
    ax.set_xlabel("n (sessions)")
    ax.set_ylabel(ylabel + (" (log scale)" if log_y else ""))
    if log_y:
        ax.set_yscale("log")
    ax.set_title(title, fontsize=12, fontweight="bold")
    ax.legend(frameon=False)
    fig.tight_layout()
    fig.savefig(f"{OUTPUT_DIR}/{filename}", bbox_inches="tight")
    plt.close(fig)


make_chart(
    "growing_family_time.png",
    "Growing-rooms family (r = n): median running time",
    "Median time (ms)",
    growing_heap_ms, growing_list_ms,
    "Heap allocator — O(n log n)",
    "List allocator — O(n\u00d7r), r = n",
)

make_chart(
    "growing_family_operations.png",
    "Growing-rooms family (r = n): operation counts",
    "Operations",
    growing_heap_ops, growing_list_ops,
    "Heap allocator",
    "List allocator",
    log_y=True,
)

make_chart(
    "fixed_family_time.png",
    "Fixed-rooms family (r = 8): median running time",
    "Median time (ms)",
    fixed_heap_ms, fixed_list_ms,
    "Heap allocator — O(n log 8)",
    "List allocator — O(n\u00d78)",
)

make_chart(
    "fixed_family_operations.png",
    "Fixed-rooms family (r = 8): operation counts",
    "Operations",
    fixed_heap_ops, fixed_list_ops,
    "Heap allocator",
    "List allocator",
    log_y=True,
)

print("done")