-- 多语言文案表
CREATE TABLE IF NOT EXISTS sys_i18n (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    module VARCHAR(50) NOT NULL DEFAULT 'common' COMMENT '模块：system/biz/common等',
    `key` VARCHAR(100) NOT NULL COMMENT '文案Key',
    zh_CN VARCHAR(500) COMMENT '中文',
    en_US VARCHAR(500) COMMENT '英文',
    ja_JP VARCHAR(500) COMMENT '日文',
    ko_KR VARCHAR(500) COMMENT '韩文',
    description VARCHAR(200) COMMENT '文案描述/使用场景',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_module_key (module, `key`),
    INDEX idx_module (module)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='多语言文案表';

-- 插入常用文案
INSERT INTO sys_i18n (module, `key`, zh_CN, en_US, ja_JP, description) VALUES
('system', 'system.success', '操作成功', 'Operation successful', '操作が成功しました', '通用成功提示'),
('system', 'system.error', '操作失败', 'Operation failed', '操作が失敗しました', '通用失败提示'),
('system', 'system.error.unknown', '系统异常，请联系管理员', 'System error, please contact administrator', 'システムエラーが発生しました。管理者に連絡してください', '系统异常提示'),
('system', 'system.param.error', '参数错误', 'Parameter error', 'パラメータエラー', '参数错误提示'),

('user', 'user.not.login', '用户未登录', 'User not logged in', 'ログインしていません', '未登录提示'),
('user', 'user.name.required', '用户名不能为空', 'Username cannot be empty', 'ユーザー名を入力してください', '用户名必填提示'),
('user', 'user.password.required', '密码不能为空', 'Password cannot be empty', 'パスワードを入力してください', '密码必填提示'),
('user', 'user.not.found', '用户不存在', 'User not found', 'ユーザーが見つかりません', '用户不存在提示'),
('user', 'user.password.error', '密码错误', 'Incorrect password', 'パスワードが正しくありません', '密码错误提示'),
('user', 'user.disabled', '用户已被禁用', 'User has been disabled', 'ユーザーは無効になっています', '用户禁用提示'),

('data', 'data.not.found', '数据不存在', 'Data not found', 'データが見つかりません', '数据不存在提示'),
('data', 'data.already.exists', '数据已存在', 'Data already exists', 'データは既に存在します', '数据已存在提示'),
('data', 'data.delete.failed', '删除失败', 'Delete failed', '削除に失敗しました', '删除失败提示'),
('data', 'data.update.failed', '更新失败', 'Update failed', '更新に失敗しました', '更新失败提示'),
('data', 'data.insert.failed', '新增失败', 'Insert failed', '追加に失敗しました', '新增失败提示'),

('serial', 'serial.number.rule.not.found', '流水号规则不存在', 'Serial number rule not found', '連番ルールが見つかりません', '流水号规则不存在'),
('serial', 'serial.number.rule.disabled', '流水号规则已禁用', 'Serial number rule disabled', '連番ルールが無効です', '流水号规则已禁用'),
('serial', 'serial.number.generate.failed', '流水号生成失败', 'Failed to generate serial number', '連番の生成に失敗しました', '流水号生成失败'),
('serial', 'serial.number.batch.count.invalid', '批量生成数量必须大于0且不超过1000', 'Batch count must be greater than 0 and not exceed 1000', 'バッチ数は1以上1000以下である必要があります', '批量生成数量无效');
