



### 依赖管理 说明

| 模块名称 | 是否需要 finalName | 说明             |
|---------|-----------------|----------------|
| pom.xml（根） | ❌ | 聚合模块，不生成 jar   |
| sledgehammer-dependencies | ❌ | BOM 文件，生成空 jar |
| sledgehammer-order | ✅ | 订单模块，生成可执行 jar |
| sledgehammer-gateway | ✅ | 网关模块，生成可执行 jar |
| sledgehammer-user | ✅ | 用户模块，生成可执行 jar |

### Nacos启动
- 启动 Nacos：Windows 在任意位置 cmd 执行：`startup.cmd -m standalone`
- 控制台地址：http://localhost:8080
- 用户名：`nacos`
- 密码：`9TVlQPhvwgXU`

### Windows 启动Ubuntu
- 启动Ubuntu：`wsl -d Ubuntu`
- 用户名：`sledgehammer`
- 密码：`root123.`
- Redis 启动：打开 Ubuntu / Windows Terminal（WSL 标签），执行一次：
- `sudo service redis-server start`
- `redis-cli ping` 验证

#### MySQL 启动
- `sudo service mysql start`
- 用户名：root
- 密码：默认是空的（blank password）
