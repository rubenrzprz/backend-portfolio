# Git Commit Guidelines

A concise, enforceable standard for consistent, high-quality commit messages.

---

## Format

\<type>(\<scope>): \<summary>

\<body>        # optional
\<footer>      # optional (e.g., Closes #12)

- **type**: one of  
  `feat` | `fix` | `docs` | `style` | `refactor` | `test` | `ci` | `chore`
- **scope** (optional): focus area, e.g. `readme`, `api`, `sql`, `docker`, `ci`, `progress`
- **summary**: imperative mood, ≤72 chars, describe **what** changed (not how)

---

## Examples

feat(api): implement items CRUD with validation  
fix(sql): correct LEFT JOIN to include users without tasks  
docs(readme): add Phase 1 overview and usage guide  
style(readme): add icons and dividers for scannability  
chore(progress): add day2-summary  
ci(actions): run tests on push for main and PRs

---

## Linking Issues

Use closing keywords **only** when the PR/commit actually completes the work.  
Use non-closing references for intermediate/linkage.

**Closing keywords (auto-close on merge)**  
- Closes #123  
- Fixes #123  
- Resolves #123

> Recommended: put the closing line in the **PR description** (or squash commit message) to be merge-strategy agnostic (squash/rebase/merge).

**Non-closing references (link, do not close)**  
- Refs #123  
- Related to #123  
- See also #123

**Multiple issues**  
- Closes #10, Closes #12  
- Refs #15, Related to #18

**Cross-repo references**  
- Closes owner/repo#123  
- Refs org/another-repo#456

---

## Body (when needed)

Explain *why* the change was made:
- Context or reasoning
- Side effects or risks
- Key tradeoffs or follow-ups

---

## Atomicity

- One logical concern per commit.  
- Prefer 2–4 focused commits over one large mixed change.  
- Separate **content** from **presentation** (e.g., `docs` vs `style`).

---

## Branching Convention

- Features → `feature/<topic>` (e.g., `feature/intro-readme`)  
- Fixes → `fix/<topic>`  
- Hotfixes → `hotfix/<topic>` (urgent)

---

## Optional: Signing

If required, sign commits with your GPG/SSH key:

git commit -S -m "feat(api): add pagination"
