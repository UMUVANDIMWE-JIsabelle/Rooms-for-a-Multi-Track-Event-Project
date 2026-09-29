# Rooms for a Multi-Track Event


Given a list of conference sessions (each with a start and end time), this
program works out the minimum number of rooms needed so that no two
overlapping sessions share a room, and which room each session goes in.

It compares two ways of finding the next free room:

- **Heap version** (`HeapRoomAllocator`) which uses a min-heap, O(n log n)
- **List version** (`ListRoomAllocator`) which uses a plain list scan, O(n×r)

Both use the same greedy rule, so they always use the same number of rooms.
The difference is speed, which is what this project measures.

## Project structure

```
data/       the two supplied CSVs (self-check and main dataset)
src/        all the Java source files
results/    scaling_results.csv, produced when you run the program
charts/     the four PNG charts, produced by make_charts.py
```

## How to run the Java program

From the project root (the folder that contains `src` and `data`):

```
javac -d out src/*.java
java -cp out Main
```

This prints, in order:
1. The self-check (should say "Self-check passed", 3 rooms)
2. The main dataset (40 sessions, room assignments, optimality check)
3. Two full experiments on 5,000 generated sessions (growing rooms, fixed rooms)
4. A scaling experiment across n = 100 to 5,000, which also writes
   `results/scaling_results.csv`

## How to generate the charts

Needs Python and matplotlib (`pip install matplotlib` if you don't have it).
Run this **after** the Java program, since it reads `results/scaling_results.csv`:

```
python make_charts.py
```

This creates four charts in a `charts/` folder:
- `growing_family_time.png` / `growing_family_operations.png`
- `fixed_family_time.png` / `fixed_family_operations.png`

## Notes

- `data/Q1_sessions_selfcheck.csv` has 8 sessions and needs 3 rooms.
- `data/Q1_sessions_main.csv` has 40 sessions and needs 10 rooms.
- The number of rooms the program finds is checked against an
  independently calculated maximum overlap (see `OptimalityChecker.java`)
  to confirm it's actually the minimum.
- Working directory matters: run `java` from the project root, not from
  inside `src`, or it won't find the `data` folder.