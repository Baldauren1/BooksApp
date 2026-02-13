## Release Notes

### Version
- VersionName: 1.1.0
- VersionCode: 2

### What changed from Endterm to Final
- Added Release vs Debug configuration:
    - Release disables verbose logging and uses production configuration
- Added signed release build workflow (AAB) and stored evidence in `/docs/release_evidence`
- Added documentation package in `/docs` (architecture, checklists, QA log, performance note)
- Improved reliability:
    - Global error handling for offline, empty states, invalid API data, and auth failures
    - Retry strategy for network operations (manual + basic automatic retry)
- Security hardening:
    - Removed secrets from repository (e.g., google-services.json excluded from Git)
    - Documented Firebase rules assumptions and user-scoped data paths
    - Input validation/sanitization for search and database writes
- Quality gates:
    - Increased unit tests to 10
    - Added release checklist (15+ items) and QA log (5 fixed issues)
- Performance:
    - Implemented at least one performance improvement and provided evidence in `/docs/PERFORMANCE_NOTE.md`

### Known limitations
- Google Books API results depend on network availability; offline mode shows only cached feed data.
- Realtime comments require internet connection and authenticated user.
- If `google-services.json` is missing locally, Firebase features will not work until it is added.
