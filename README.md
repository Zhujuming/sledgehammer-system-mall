



### 依赖管理 说明

| 模块名称 | 是否需要 finalName | 说明 |
|---------|-----------------|------|
| pom.xml（根） | ❌ | 聚合模块，不生成 jar |
| sledgehammer-dependencies | ❌ | BOM 文件，生成空 jar |
| sledgehammer-order | ✅ | 业务模块，生成可执行 jar |
| sledgehammer-gateway | ✅ | 业务模块，生成可执行 jar |