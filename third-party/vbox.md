# 修改快照上下级
1. 备份配置信息 `vms/{vm-name}/`
    ```bash
    # window 直接手动操作桌面即可！

    # linux vms/{vm-name}/ 
    cp vm-name.vbox vm-name.vbox.bak
    mv vm-name.vbox-prev vm-name.vbox-prev.bak
    ```
2. 重新配置`vm-name.vbox`
   1. 修改`MediaRegistry->HardDisks`配置项，将需要调整的硬盘按上下级关系进行调整
   2. 修改`Snapshot->Snapshots`配置项，同样按上下级关系调整
3. 命令修改磁盘上级映射(父级UUID存储在磁盘上，需要命令修改磁盘数据)`VBoxManage internalcommands sethdparentuuid`
    ```bash
    # snapshot-path/ 快照磁盘具体路径
    VBoxManage internalcommands sethdparentuuid "<snapshot-path/>{mediu-uuid}.vdi" "parent-mediu-uuid"
    ```
4. 重新启动vbox

**注意**：
* **最顶层快照的磁盘配置不能变更**，直接绑定虚拟机主磁盘(`当然可以尝试修改vm信息`)
* **先备份后修改，记清上下级**
* **提示**：`vm/snapshot/medium`关联信息可通过命令查看，也可直接通过配置查看
  * `vboxmanage snapshot vm-name list` # 查看快照列表(`env.dongle.com`)
    ```bash
    # vboxmanage snapshot env.dongle.com list
        Name: init (UUID: d29df676-d481-4e27-9fa6-b9daab0a32b9)
            Name: build (UUID: 3ad662f7-9d07-4bc9-be71-766b256fb625)
            Name: torch (UUID: b47935e5-80c0-4d6b-bd39-feafa0260475) *
    ```
  * `vboxmanage snapshot vm-name showvminfo snap-uuid` 查看快照详情，里面有对应硬盘UUID
    * 如快照`b47935e5-80c0-4d6b-bd39-feafa0260475`对应磁盘为`5fd54efd-24df-47f2-add1-fcf42c1b6179`(详见以下信息)
    ```bash
    # vboxmanage snapshot env.dongle.com showvminfo b47935e5-80c0-4d6b-bd39-feafa0260475
    Storage Controllers:
    #0: 'IDE', Type: PIIX4, Instance: 0, Ports: 2 (max 2), Bootable
        Port 0, Unit 0: Empty
    #1: 'SATA', Type: IntelAhci, Instance: 0, Ports: 1 (max 30), Bootable
        Port 0, Unit 0: UUID: 5fd54efd-24df-47f2-add1-fcf42c1b6179
        Location: "Snapshots/{5fd54efd-24df-47f2-add1-fcf42c1b6179}.vdi"
    NIC 1:                       MAC: 080027B8CA42, Attachment: NAT, Cable connected: on, Trace: off (file: none), Type: 82540EM, Reported speed: 0 Mbps, Boot priority: 0, Promisc Policy: deny, Bandwidth group: none
    ```
  * `vboxmanage showmediuminfo medium-uuid` 查看磁盘信息,了解初始父级磁盘UUID
    * 如磁盘`5fd54efd-24df-47f2-add1-fcf42c1b6179`对应父级为`d90116ad-029e-4ab8-8ae8-bd52d606681c`(详见以下信息)
    ```bash
    # vboxmanage showmediuminfo 5fd54efd-24df-47f2-add1-fcf42c1b6179
    UUID:           5fd54efd-24df-47f2-add1-fcf42c1b6179
    Parent UUID:    d90116ad-029e-4ab8-8ae8-bd52d606681c
    State:          created
    Type:           normal (differencing)
    Auto-Reset:     off
    Location:       D:\Data\DevOps\vm\deploy\env.dongle.com\Snapshots/{5fd54efd-24df-47f2-add1-fcf42c1b6179}.vdi
    ```