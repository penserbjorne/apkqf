# What is this?

This is a code review for [androidqf](https://github.com/botherder/androidqf).
The intention of this review is to understand what the tool does and how it works so its
functionalities can be implemented on [apkqf](https://github.com/penserbjorne/apkqf).

For this revision (and further revisions) I made a fork from the original project repository on
GitHub and created a branch for it called
[apkqf-ref-rev1](https://github.com/penserbjorne/androidqf/blob/apkqf-ref-rev1). So this can works
as a cold reference for the current development of _apkqf_ on its _version 01_.

If there is any new code on _androidqf_ then the [fork](https://github.com/penserbjorne/androidqf)
will be updated and a new branch for the next revision will be added.

# androidqf revision 001

## The _main_ function

The entry point or `main` function for the tool is on the
[`/cmd/main.go` file](https://github.com/penserbjorne/androidqf/blob/apkqf-ref-rev1/cmd/main.go).

The list of actions to be done are:

- Start acquisition
- Creation of folders to store information
- Getting a backup
- Getting properties from device
- Getting settings from device
- Getting current processes on execution
- Getting current services on execution
- Getting `logcat` information
- Getting logs from device
- Getting `dumpsys` information
- Download APKs
- Complete acquisition
- Store securely the acquisition

## Acquisition

The whole information acquisition is done through the
[`acquisition` module](https://github.com/penserbjorne/androidqf/tree/apkqf-ref-rev1/acquisition)
and the
[`adb` module](https://github.com/penserbjorne/androidqf/tree/apkqf-ref-rev1/adb).

The `acquisition` module has the classes and methods for every task to be done by the tool
(mentioned above in the last section).

The `adb` module works as an intermediate to locate the `adb` binary and to execute `adb` commands.

## Start acquisition

[Code reference](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/acquisition.go#L32).

- A new object for the acquisition is created.
- It initialize the for this acquisition `UUID` and a timestamp labeled as `Started`.
- It initialize `adb`. In our case we don't need `adb`so we can skip it.
- [It creates the necessary folders](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/acquisition.go#L71).
  - `StoragePath`, the main working directory to store the information. This folder will be named as
  the `UUID`.
  - `LogsPath`, the store the retrieved `logs`. This folder will be inside the `StoragePath` folder
  its name will be _logs_.
  - `APKSPath`, the store the retrieved `apks`. This folder will be inside the `StoragePath` folder
	its name will be _logs_.

## Backup

[Code reference](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/backup.go#L23).

For backups we need to select an option between:

- Only SMS (`backupOnlySMS`)
- Everything (`backupEverything`)
- Nothing (`backupNothing`)
  
[The execution is made through `adb` with the `backup` command and the selected option](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/adb/adb.go#L69).

- Only SMS (`backupOnlySMS`) --> `adb backup com.android.providers.telephony`
- Everything (`backupEverything`) --> `adb backup -all`
- Nothing (`backupNothing`) --> This doesn't execute nothing.

Then the generated backup (`backup.ab`) is copied to the current working folder (`StoragePath`).

In our case, we only want to focus on implement the `backupOnlySMS` option.

## GetProp

[Code reference](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/getprop.go#L12).

The device properties are obtained by `adb` wth the `adb shell getprop` command.

The command result is stored in the `getprop.txt` file in the working directory (`StoragePath`).

## Settings (Need verifications!)

[Code reference](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/settings.go#L12).
This task obtain the device configurations. There are three levels of configurations:

- `system` configurations
- `secure` configurations
- `global` configurations

The configurations are obtained by `adb` with the `cmd settings list [configuration_type]` command.

- `system` configurations --> `cmd settings list system`
- `secure` configurations --> `cmd settings list secure`
- `global` configurations --> `cmd settings list global`

The result of every command is stored in their respective file (`settings_[configuration_type].txt`)
in the working directory (`StoragePath`).

- `system` configurations --> `cmd settings list system` --> `settings_system.txt`
- `secure` configurations --> `cmd settings list secure` --> `settings_secure.txt`
- `global` configurations --> `cmd settings list global` --> `settings_global.txt`

**Note!** It seems that the correct commands are:

- `adb shell settings list system`
- `adb shell settings list secure`
- `adb shell settings list global`

**This need to be verified.**

## Processes (Need verifications!)

[Code reference](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/processes.go#L12).

The list of the current processes on execution are obtained by `adb` with the `adb shell ps -A`
command.

The command result is stored in the `ps.txt` file in the working directory (`StoragePath`).

**Note!** Only works `adb shell ps`, if we use the `-A` flag we get `bad pid '-A'`.

## Services (Need verifications!)

[Code reference](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/services.go#L12).

The list of the current services on execution are obtained by `adb` with the `adb shell service list`
command.

The command result is stored in the `services.txt` file in the working directory (`StoragePath`).

**Note!** The output from this command is not ordered, but on `androidqf` output it is ordered.

## Logcat

[Code reference](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/logcat.go#L12).

The `logcat` information is obtained by `adb` with the `adb shell logcat -d -b all` command.

The command result is stored in the `logcat.txt` file in the working directory (`StoragePath`).
	
## Logs (Need verifications!)

[Code reference](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/logs.go#L17).

These files are extracted as logs:

- `/data/system/uiderrors.txt`
- `/proc/kmsg`
- `/proc/last_kmsg`
- `/sys/fs/pstore/console-ramoops`

And also all the files from this two folders:

- `/data/anr/`
- `/data/log/`
	
The files are obtained by `adb`with the `adb pull remote_path local_path` command for every file. 

The files are stored in the `logs` folder in the working directory (`LogsPath`).

It is necessary to consider that some files wouldn't be accessible.

**Note!** From emulator I was not able to get access to `/data/` folder. I need to test on a
physical device.

## DumpSys

[Code reference](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/dumpsys.go#L12).

The `dumpsys` information is obtained by `adb` with the `adb shell dumpsys` command.

The command result is stored in the `dumpsys.txt` file in the working directory (`StoragePath`).

## DownloadAPKs (ToDo)

https://github.com/penserbjorne/androidqf/blob/apkqf-ref-rev1/adb/packages.go
https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/apks.go#L51

## Complete (ToDo)

[Code reference](https://github.com/penserbjorne/androidqf/blob/6302f2db0c4796a314ba21f8875ad1b94075c194/acquisition/acquisition.go#L51).

It stores the current timestamp as the end of the acquisition.


## StoreSecurely (ToDo)

