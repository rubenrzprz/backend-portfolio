# GIT_GUIDELINES

## Branch Naming
- Features: `feature/<topic>` (e.g., `feature/git-practice`)
- Fixes: `fix/<topic>`
- Hotfix: `hotfix/<topic>` (urgent)

## Commit Frequency
- Prefer **small, atomic commits** (1 logical change).
- Separate content vs presentation (e.g., `docs` vs `style`).

## Sync Strategy (Local)
- **Preferred:** `git pull --rebase` to keep a linear branch history.
- `git merge` may be used for learning, but avoid merge commits on long-lived branches.

## PR Policy
- **Default merge strategy:** **Squash & merge** into `main`.
- Put closing keywords in the **PR description** (e.g., `Closes #123`) to be merge-strategy agnostic.
- Keep scope **small**; reference tracking issues with `Refs #id`.

## Branch Cleanup
- Enable “delete head branches” on merge (GitHub).
- Locally: `git fetch --prune`; delete merged branches with `git branch -d <branch>`.

## When to Rebase vs Merge (Learning Note)
- Use **rebase** to replay your work on top of updated `main`.
- Understand **merge** to recognize merge commits; avoid merging into `main`.