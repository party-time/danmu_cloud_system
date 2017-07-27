package cn.partytime.model;

/**
 * Created by administrator on 2017/7/24.
 */
public class Monitor {

        private String id;

        //监控标题
        private String title;

        //管理员的ids使用逗号分隔
        private String adminUserIds;

        //告警内容 有占位符需要替换掉
        private String content;

        //告警的英文标示
        private String key;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAdminUserIds() {
            return adminUserIds;
        }

        public void setAdminUserIds(String adminUserIds) {
            this.adminUserIds = adminUserIds;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

}
