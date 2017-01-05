/**
 * 
 */
package __class;

/**
 * @author Administrator
 *
 */

public enum ApiURL {
	
	EDU_LOGIN(
            "http://api.sky31.com/edu-new/student_info_all.php",
            ""
    ),
	MYSQL_GET_CHAT_RECORD(
			"http://api.kilingzhang.com/diantalk/getChatRecord.php",
			""
	),
	MYSQL_GET_FRIEND_LIST(
			"http://api.kilingzhang.com/diantalk/getUserFriendList.php",
			""
	),
	MYSQL_GET_USER_INFO(
			"http://api.kilingzhang.com/diantalk/getUserInfo.php",
			""
	),
	MYSQL_SET_USER_INFO(
			"http://api.kilingzhang.com/diantalk/setUserInfo.php",
			""
	),
	MYSQL_SET_FRIEND_LIST(
			"http://api.kilingzhang.com/diantalk/setFriendList.php",
			""
	),
	MYSQL_GET_GROUP_LIST(
			"http://api.kilingzhang.com/diantalk/getUserGroupList.php",
			""
	),
	MYSQL_GET_GROUP_INFO(
			"http://api.kilingzhang.com/diantalk/getGroupInfo.php",
			""
	), MYSQL_ADD_GROUP_USER(
			"http://api.kilingzhang.com/diantalk/addGroupUser.php",
			""
	), CREATE_GROUP(
			"http://api.kilingzhang.com/diantalk/createGroup.php",
			""
	),DianCha(
			"http://www.kilingzhang.com/DianCha/",
			""
	),MUSICURL(
			"https://diyitem.sky31.com/Music/Music163.php?name=",
			""
	),
	;
	
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";

    private String url;

    private String referer;

    ApiURL(String url, String referer) {
        this.url = url;
        this.referer = referer;
    }

    public String getUrl() {
        return url;
    }


    public String getReferer() {
        return referer;
    }

    public String buildUrl(Object... params) {
        int i = 1;
        String url = this.url;
        for (Object param : params) {
            url = url.replace("{" + i++ + "}", param.toString());
        }
        return url;
    }

    public String getOrigin() {
        return this.url.substring(0, url.lastIndexOf("/"));
    }
}
