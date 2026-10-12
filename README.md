# ORVESS Client — Alpha 0.3 working project

This is a web app/PWA project, not an installable Android APK. The uploaded file had a ZIP archive containing HTML, CSS, JavaScript, icons and a service worker.

## Changes in this update
- Added conservative ZIP archive checks: maximum 5,000 files, maximum 300 MB declared expanded size, and maximum 100 MB per individual file.
- Improved the error message when JSZip cannot load.
- Bumped the service-worker cache name so browsers can replace the old app shell.

## Known limitation
JSZip is still loaded from a CDN in `index.html`. Therefore, first-time pack inspection needs internet, and pack inspection may fail offline if the library was not cached. Do not claim fully offline pack inspection yet. Bundle a reviewed local copy of JSZip in a later step.

## Use from an Android phone
1. Extract this ZIP using your file manager or an archive app.
2. Edit `index.html` and `sw.js` in Acode or another text editor.
3. To test the PWA/service worker, host the folder on HTTPS (for example, GitHub Pages). Opening `index.html` directly from local storage may not enable service-worker features.
4. After publishing changes, refresh the page. If an old version persists, clear that site's data or unregister its service worker, then reload.

## Safety note
Archive size checks use ZIP metadata supplied by JSZip. They are a useful guardrail, not a complete security audit. Only inspect files you trust, and test large archives carefully on a phone.
