# 宿舍管理接口测试文档

## 基础信息
- 服务地址：`http://localhost:8080`
- 基础路径：`/api/room`

---

## 1. 分页查询宿舍列表

**接口地址：** `GET /api/room/list`

**请求参数：**
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| pageNum | int | 否 | 1 | 页码（从1开始） |
| pageSize | int | 否 | 10 | 每页数量 |

**请求示例：**
```bash
curl -X GET "http://localhost:8080/api/room/list?pageNum=1&pageSize=10"
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "total": 20,
    "records": [
      {
        "id": 1,
        "buildingId": 1,
        "buildingName": "1号楼",
        "roomNum": "101",
        "capacity": 4,
        "occupied": 2,
        "createTime": "2024-01-01T10:00:00",
        "updateTime": "2024-01-01T10:00:00"
      }
    ]
  }
}
```

---

## 2. 新增宿舍

**接口地址：** `POST /api/room`

**请求头：**
```
Content-Type: application/json
```

**请求体：**
```json
{
  "buildingId": 1,
  "roomNum": "101",
  "capacity": 4
}
```

**字段说明：**
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| buildingId | Long | 是 | 楼宇ID |
| roomNum | String | 是 | 房号 |
| capacity | Integer | 是 | 床位数（需大于0） |
| occupied | Integer | 否 | 已住人数（默认为0） |

**请求示例：**
```bash
curl -X POST "http://localhost:8080/api/room" \
  -H "Content-Type: application/json" \
  -d '{
    "buildingId": 1,
    "roomNum": "101",
    "capacity": 4
  }'
```

**成功响应：**
```json
{
  "code": 200,
  "msg": "新增成功",
  "data": null
}
```

**失败响应示例：**
```json
{
  "code": 500,
  "msg": "该楼宇下房号已存在",
  "data": null
}
```

---

## 3. 修改宿舍

**接口地址：** `PUT /api/room/{id}`

**路径参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 宿舍ID |

**请求头：**
```
Content-Type: application/json
```

**请求体：**
```json
{
  "buildingId": 1,
  "roomNum": "102",
  "capacity": 6,
  "occupied": 3
}
```

**字段说明：**
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| buildingId | Long | 否 | 楼宇ID |
| roomNum | String | 否 | 房号 |
| capacity | Integer | 否 | 床位数（需大于0） |
| occupied | Integer | 否 | 已住人数（不能超过床位数） |

**注意：** 只传需要修改的字段即可，使用动态SQL更新。

**请求示例：**
```bash
curl -X PUT "http://localhost:8080/api/room/1" \
  -H "Content-Type: application/json" \
  -d '{
    "capacity": 6,
    "occupied": 3
  }'
```

**成功响应：**
```json
{
  "code": 200,
  "msg": "修改成功",
  "data": null
}
```

**失败响应示例：**
```json
{
  "code": 500,
  "msg": "已住人数不能超过床位数",
  "data": null
}
```

---

## 4. 删除宿舍

**接口地址：** `DELETE /api/room/{id}`

**路径参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 宿舍ID |

**请求示例：**
```bash
curl -X DELETE "http://localhost:8080/api/room/1"
```

**成功响应：**
```json
{
  "code": 200,
  "msg": "删除成功",
  "data": null
}
```

**失败响应示例：**
```json
{
  "code": 500,
  "msg": "该宿舍仍有学生居住，无法删除",
  "data": null
}
```

---

## 测试步骤建议

### 前置准备
1. 确保数据库已初始化（执行 `database_init.sql`）
2. 确保至少有一条楼宇数据（building表）

### 测试流程

1. **测试新增宿舍**
   ```bash
   # 新增第一个宿舍
   curl -X POST "http://localhost:8080/api/room" \
     -H "Content-Type: application/json" \
     -d '{"buildingId": 1, "roomNum": "101", "capacity": 4}'
   
   # 测试重复房号（应该失败）
   curl -X POST "http://localhost:8080/api/room" \
     -H "Content-Type: application/json" \
     -d '{"buildingId": 1, "roomNum": "101", "capacity": 4}'
   ```

2. **测试分页查询**
   ```bash
   curl -X GET "http://localhost:8080/api/room/list?pageNum=1&pageSize=10"
   ```

3. **测试修改宿舍**
   ```bash
   # 修改床位数
   curl -X PUT "http://localhost:8080/api/room/1" \
     -H "Content-Type: application/json" \
     -d '{"capacity": 6}'
   
   # 测试已住人数超过床位数（应该失败）
   curl -X PUT "http://localhost:8080/api/room/1" \
     -H "Content-Type: application/json" \
     -d '{"occupied": 10}'
   ```

4. **测试删除宿舍**
   ```bash
   # 先新增一个空宿舍用于删除测试
   curl -X POST "http://localhost:8080/api/room" \
     -H "Content-Type: application/json" \
     -d '{"buildingId": 1, "roomNum": "999", "capacity": 4}'
   
   # 删除空宿舍（应该成功）
   curl -X DELETE "http://localhost:8080/api/room/2"
   
   # 删除有学生的宿舍（应该失败）
   # 先修改occupied > 0，再尝试删除
   ```

---

## 使用 Postman 测试

### 导入 Collection（可选）

可以创建 Postman Collection，包含以上所有接口。

### 环境变量设置
- `base_url`: `http://localhost:8080`

### 测试用例

1. **新增宿舍** - POST `{{base_url}}/api/room`
2. **查询列表** - GET `{{base_url}}/api/room/list?pageNum=1&pageSize=10`
3. **修改宿舍** - PUT `{{base_url}}/api/room/1`
4. **删除宿舍** - DELETE `{{base_url}}/api/room/1`

---

## 注意事项

1. **唯一约束**：同一楼宇下房号不能重复
2. **删除限制**：已住人数大于0的宿舍不能删除
3. **数据校验**：
   - 床位数必须大于0
   - 已住人数不能超过床位数
   - 楼宇ID必须存在
4. **动态更新**：修改接口使用XML动态SQL，只更新传入的字段







