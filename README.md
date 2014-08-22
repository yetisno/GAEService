# Description #

Make your Google App Engine as a Key-Value Datastore with simple RESTful API.

### Flow & API ###

    Add User ->
        Get Token ->
        Renew token ->
            Put Key-Value Entity
            Get Key-Value Entity
            Delete Key-Value Entity
    Delete User

# Install #

## Step 1. Configure File ##
1. Copy src/main/webapp/WEB-INF/appengine-web.xml.sample to src/main/webapp/WEB-INF/appengine-web.xml

    	Change <application>Application_ID</application> to your Application ID in src/main/webapp/WEB-INF/appengine-web.xml.

2. Copy src/main/resources/config.json.sample to src/main/resources/config.json

    	Change ADKey's value to your private key in src/main/resources/config.json, which use to do important operation(EX: add user, delete user).

## Step 2. Deploy to Google App Engine ##

# API #


## Add User ##
### Request ###
`POST` 	/users/{:userID}?adk=:ADKey&pa=:passphrase

| Param Name | Type | Description
| --- | --- | --- |
| `adk` | *String* | Admin Key |
| `pa` | *String* | Passphrase |

### Response ###
| Param Name | Type | Description
| --- | --- | --- |
| `tkn` | *String* | Access Token |
| `t` | *long* | Expire Timestamp |
| `s` | *long* | Status Code |
``` json
    {
        tkn: Access Token (String),
        t: Timestamp (long),
        s:  Status Code (long)
    }
```

### Example ###
#### Request ####
`POST`  /users/yeti?adk=myadkey&pa=mypassphrase

#### Response ####
``` json
    {
        tkn: "b8b011b4-d735-4dd1-bf52-0e0730b96b8d",
        t: 1408813726166,
        s: 0
    }
```
#### Error Response ####
``` json
{
    s: 2
    m: "Service UNAuthorized."
}
```

---
## Delete User ##
### Request ###
`DELETE` 	/users/{:userID}?adk=:ADKey

| Param Name | Type | Description
| --- | --- | --- |
| `adk` | *String* | Admin Key |

### Response ###
| Param Name | Type | Description
| --- | --- | --- |
| `s` | *long* | Status Code |
``` json
    {
        s:  Status Code (long)
    }
```
### Example ###
#### Request ####
`DELETE`  /users/yeti?adk=myadkey
#### Response ####
``` json
    {
        s: 0
    }
```
#### Error Response ####
``` json
{
    s: 2
    m: "Service UNAuthorized."
}
```

---
## Get Token ##
### Request ###
`GET` 	/tkn/?ui=:userID&pa=:passphrase

| Param Name | Type | Description
| --- | --- | --- |
| `ui` | *String* | User ID |
| `pa` | *String* | Passphrase |

### Response ###
| Param Name | Type | Description
| --- | --- | --- |
| `tkn` | *String* | Access Token |
| `t` | *long* | Expire Timestamp |
| `s` | *long* | Status Code |
``` json
    {
        tkn: Access Token (String),
        t: Timestamp (long),
        s:  Status Code (long)
    }
```

### Example ###
#### Request ####
`GET`  /tkn/?ui=yeti&pa=mypassword

#### Response ####
``` json
    {
        tkn: "b8b011b4-d735-4dd1-bf52-0e0730b96b8d",
        t: 1408813726166,
        s: 0
    }
```
#### Error Response ####
``` json
{
    s: 2
    m: "Service UNAuthorized."
}
```

---
## Renew Token ##
### Request ###
`POST` 	/tkn/{:token}

| Param Name | Type | Description
| --- | --- | --- |
| `token` | *String* | Access Token |

### Response ###
| Param Name | Type | Description
| --- | --- | --- |
| `tkn` | *String* | Access Token |
| `t` | *long* | Expire Timestamp |
| `s` | *long* | Status Code |
``` json
    {
        tkn: Access Token (String),
        t: Timestamp (long),
        s:  Status Code (long)
    }
```

### Example ###
#### Request ####
`POST`  /tkn/aca011b4-d735-4dd1-bf52-0e0a30b96b8d

#### Response ####
``` json
    {
        tkn: "b8b011b4-d735-4dd1-bf52-0e0730b96b8d",
        t: 1408813726166,
        s: 0
    }
```
#### Error Response ####
``` json
{
    s: 2
    m: "Service UNAuthorized."
}
```

---
## Get KVEntity(Key-Value Entity) ##
### Request ###
`GET` 	/kv/{:kvKey}?tkn=:token

| Param Name | Type | Description
| --- | --- | --- |
| `kvKey` | *String* | KVEntity Key |
| `tkn` | *String* | Access Token |

### Response ###
| Param Name | Type | Description
| --- | --- | --- |
| `k` | *String* | KVEntity Key |
| `n` | *String* | KVEntity Name |
| `t` | *long* | Update Timestamp |
| `v` | *String* † | KVEntity value |
| `s` | *long* | Status Code |

† Encoded By Base64.

``` json
    {
        k: KVEntity Key (String),
        n: KVEntity Name (String),
        t: Update Timestamp (long),
        v: KVEntity value (String)†,
        s: Status Code (long)
    }
```

### Example ###
#### Request ####
`GET`  /kv/myKVEntityKey1

#### Response ####
``` json
    {
        k: "myKVEntityKey1",
        n: "myKVEntityKey1Name",
        t: 1408813726166,
        v: "EBSDF=",
        s: 0
    }
```
#### Error Response ####
``` json
{
    s: 2
    m: "Service UNAuthorized."
}
```

---
## PUT KVEntity(Key-Value Entity) ##
### Request ###
`POST` 	/kv/{:kvKey}?tkn=:token&n=:name

Body (Value)

    RUJTMzUzU0FTRGFzZERGPQ †

† Encoded By Base64.

| Param Name | Type | Description
| --- | --- | --- |
| `kvKey` | *String* | KVEntity Key |
| `tkn` | *String* | Access Token |
| `n` | *String* | KVEntity Name |

### Response ###
| Param Name | Type | Description
| --- | --- | --- |
| `s` | *long* | Status Code |
``` json
    {
        s: Status Code (long)
    }
```

### Example ###
#### Request ####
`POST`  /kv/myKVEntityKey1?tkn=myToken&n=myKVEntityKey1Name

#### Response ####
``` json
    {
        s: 0
    }
```
#### Error Response ####
``` json
{
    s: 2
    m: "Service UNAuthorized."
}
```

---
## Delete KVEntity(Key-Value Entity) ##
### Request ###
`DELETE` 	/kv/{:kvKey}?tkn=:token

| Param Name | Type | Description
| --- | --- | --- |
| `kvKey` | *String* | KVEntity Key |
| `tkn` | *String* | Access Token |

### Response ###
| Param Name | Type | Description
| --- | --- | --- |
| `s` | *long* | Status Code |
``` json
    {
        s: Status Code (long)
    }
```

### Example ###
#### Request ####
`DELETE`  /kv/myKVEntityKey1?tkn=myToken

#### Response ####
``` json
    {
        s: 0
    }
```
#### Error Response ####
``` json
{
    s: 2
    m: "Service UNAuthorized."
}
```
