# ninja-vs-zombie
Android/Desktop game made with libGDX

Android:
* create local.properties file with your android SKD path
`sdk.dir=C\:\\Users\\*YOUR_USERNAME*\\AppData\\Local\\Android\\Sdk`
* package .apk
`use android-package-apk.bat`
* generated .apk file path
`.../android/build/outputs/apk`

Desktop:
* package .jar
`use desktop-package-jar.bat`
* generated .jar file path
`desktop/build/libs/`
* run
`use desktop-run.bat`

Features:
* Keys: LEFT, RIGHT, SPACE or screen buttons
* Random generate male/female zombies and their start side of screen
* Zombies can walk, fight, dead
* Player can idle, run, fight, dead
* Every behaviour has its own animation
* Zombies follow player
* Zombies dying after 3 hits
* Player health bar
* Killed zombies counter
* FPS counter
* Debug mode (actor bounds, drag'n'drop player)

<table>
    <tr>
        <td>
            <img src="http://i.imgur.com/vTVokMw.png" width="500">
        </td>
        <td>
            <img src="http://i.imgur.com/0P37rBC.png" width="500">
        </td>
    </tr>
    <tr>
        <td>
            <img src="http://i.imgur.com/cjgQ1xC.png" width="500">
        </td>
    </tr>
</table>