# RetrofitRealmPicLoad
这是我大一刚学Android的时候，星空布置的一个小练习。

涵盖`Retrofit网络请求框架`+`Realm数据库`+`Glide图片框架`+`Gson`等等的知识。

因为最近有朋友说需要，就放上来了😄

## 使用说明
本应用目前是一个`fir.im`的查看应用列表Demo，具体API是用方法请看[fir.im官方文档](https://fir.im/docs/apps)

需要去把`us.xingkong.retrofitrealmpicload.api.APIService.java`中的`{Your API Token}`替换为自己的Token。

## 思路
请求`fir.im`的应用列表数据，
把每个应用的基本信息加载到`RecyclerView`的`item`中，
图片用两个图片框架(`Glide`/`Picasso`)其中一个加载出来，
并把信息缓存到本地，
当没有网络时获取本地数据库数据并加载到列表中去。

## 项目结构
`api`包主要存放网络请求接口
`bean`包中存放实体类
`model`包中存放处理数据
`presenter`包中存放主导器
`view`包中存放界面处理

## 数据格式
示例：

```json
{
    "apps_count": 3,
    "page_size": 20,
    "items": [
        {
            "id": "5540802dadd5b62426000cbc",
            "user_id": "5598da0b692d686ff00008d0",
            "type": "ios",
            "name": "iOS",
            "short": "iOSoid",
            "bundle_id": "im.fir.BugHDClient",
            "genre_id": 14,
            "is_opened": true,
            "web_template": "default",
            "has_combo": false,
            "created_at": 1430290477,
            "icon_url": "http://firicon.fir.im/ddb329b670814bfeaa67d5d9bf73502d4e49e560",
            "master_release": {
                "version": "1.0",
                "build": "2",
                "release_type": "adhoc",
                "distribution_name": "",
                "supported_platform": null,
                "created_at": 1443151607
            }
        },
        {
            "id": "55420f6525fc666a7300023c",
            "user_id": "5598da29692d686ff0000c64",
            "type": "android",
            "name": "BugHD",
            "short": "xr66",
            "bundle_id": "com.bughd.client",
            "genre_id": 0,
            "is_opened": false,
            "web_template": "default",
            "has_combo": false,
            "created_at": 1430392677,
            "icon_url": "http://firicon.fir.im/166ad4e2cf6e13d2e8d9ca4950fcd7574dd72755",
            "master_release": {
                "version": "1.0",
                "build": "30",
                "release_type": "",
                "distribution_name": "",
                "supported_platform": null,
                "created_at": 1443079076
            }
        },
        {
            # more...
        }
    ]
}
```