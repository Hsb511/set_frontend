# set_frontend
This is the frontend client. For the specs see [souchel/set](https://github.com/users/souchel/projects/1). For the backend see [tsouche/set_backend
](https://github.com/tsouche/set_backend)

## Release process

### Android 

#### Debug

To generate debug artifacts, in Android Studio:

> \> Build > Generate App Bundles or APKs > Generate APKs

#### Release

To generate release artifacts, in the terminal run the commands:

```console
export KEYSTORE_PATH="[VALUE]"
export KEYSTORE_PASSWORD="[VALUE]"
export KEY_ALIAS="[VALUE]"
export KEY_PASSWORD="[VALUE]"
```
To generate .apk: 
```console
./gradlew :androidApp:assembleRelease
```

To generate .aab:
```console
./gradlew :androidApp:bundleRelease
```

#### Generated artifacts

The generated apk is located under:

> \> androidApp > build > outputs > apk > debug or release

Then simply rename with format v1.8.0-debug.apk