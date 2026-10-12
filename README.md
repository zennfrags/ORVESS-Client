# ORVESS Client — Alpha 0.4

This is a web app/PWA project, not an installable Android APK or a native injected Minecraft client.

## Alpha 0.4 additions
- Added a Modules screen accessible from Home and Settings.
- Added persistent local preferences for Floating Menu, Crosshair Tools, FPS HUD and Armor HUD.
- Added explicit status labels to distinguish planned modules from implemented features.
- Added user-facing notices explaining that these preference toggles do not activate features inside Minecraft yet.
- Preserved the resource-pack inspector, history, search/filter, JSON export and Minecraft launch button.
- Updated the service-worker cache version.

## Current limitations
- Floating Menu is a UI preference only; a true Android overlay requires a native Android host and user-granted overlay permission.
- Crosshair Tools, FPS HUD and Armor HUD are not connected to Minecraft gameplay.
- The Minecraft launch button opens the installed Bedrock app using an Android intent, but the browser cannot reliably confirm whether the game successfully launched.
- JSZip is loaded from a CDN, so first-time pack inspection requires internet and offline inspection is not guaranteed.
- Pack inspection is structural and does not guarantee compatibility with Minecraft.

## Use from an Android phone
1. Keep a backup of the previous project.
2. Extract this ZIP.
3. Open `index.html`, `sw.js` and other files in Acode to inspect or edit them.
4. To test PWA and service-worker behavior, host the folder on HTTPS (for example, GitHub Pages). Opening `index.html` directly from local storage may not enable all PWA features.
5. Open the Modules screen and toggle a preference. Leave and return to the screen or reopen the app to check that the setting persists.

## Archive inspection safeguards
The inspector applies limits to archive size, file count and declared uncompressed sizes. These are guardrails, not a complete security audit. Inspect only files you trust.

Not an official Minecraft product. Not approved by or associated with Mojang or Microsoft.
