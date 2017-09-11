## Ninja vs Zombie

### Description
Android/Desktop 2d platform fully animated game

### Tools
Java, libGDX, Photoshop

### Features
- keys/touch steering
- random generating enemies
- enemies following player
- health/killed bars
- FPS counter
- debug mode (actor bounds, drag’n’drop player)

### Screenshots
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

### Build

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