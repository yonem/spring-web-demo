package jp.ne.yonem.util;

/**
 * アプリケーションで使用するURLの定数クラス
 *
 * <ul>
 *     <li>無印：アドレスバーに入力されるURL</li>
 *     <li>PARAM_で始まる定数：パラメータ用</li>
 *     <li>VIEW_で始まる定数：ビュー用</li>
 * </ul>
 */
public class URL {

    /**
     * ホスト直でURLを指定した場合
     */
    public static final String NON = "/";

    /**
     * ユーザ登録画面のURL
     */
    public static final String SIGN_UP = "/sign_up";

    /**
     * ログイン画面のURL
     */
    public static final String LOGIN = "/login";

    /**
     * ログアウトパラメータ
     */
    public static final String PARAM_LOGOUT = "logout";

    /**
     * エラーパラメータ
     */
    public static final String PARAM_ERROR = "error";

    /**
     * TODO一覧画面のURL
     */
    public static final String HOME = "/home";

    /**
     * TODO削除のURL
     */
    public static final String TODO_DELETE = "/todo_delete";

    /**
     * TODO一覧画面のビュー
     */
    public static final String VIEW_TODO = "todo/list";
}
