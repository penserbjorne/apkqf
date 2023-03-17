# apkqf

`apkqf` (APK Quick Forensics) is an Android application to simplify the acquisition of relevant
forensic data from Android devices. It is inspired on
[androidqf](https://github.com/botherder/androidqf).

`apkqf` is intended to provide a simple utility to quickly acquire data from Android devices.  It
is similar in functionality to [androidqf](https://github.com/botherder/androidqf) and 
[mvt-android](https://github.com/mvt-project/mvt). However, contrary to `androidqf` and `MVT`,
`apkqf` is designed to be easily run by non-tech savvy users as well.

<!--[Download apkqf](ToDo)-->

![](apkqf.png)

## How to use

- Install the application from [ToDo](ToDo).
- Press the `RUN EXTRACTION` button.
- Wait a few minutes until the application ends the acquisition. It will depends on the amount of
information on your device and the characteristics of it.
- A `.zip` file will be created, share it with your technician of trust or save it on a secure 
platform.

The following data can be extracted:

- A list of all packages installed and related distribution files.
- The output of the `dumpsys` shell command, providing diagnostic information about the device.
- The output of the `getprop` shell command, providing build information and configuration parameters.
- All system settings.
- The output of the `ps` shell command, providing a list of all running processes.
- A backup of SMS and MMS messages.

## Build

To build `apkqf` you will need [Android Studio Electric Eel](https://developer.android.com/studio).

You need to clone the repository:

```bash
git clone https://github.com/penserbjorne/apkqf.git
```

Open the `apkqf` folder inside the repository with `Android Studio`.

On `Android Studio` you need to configure an `emulator` or a physical device. For more information
you can read the [Android Developer Documentation](https://developer.android.com/studio/run).