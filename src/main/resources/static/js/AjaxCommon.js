/**
 * 
 * 共通のAjax通信関数
 * 
 * @param {Object} options - 通信オプション
 * @param {string} options.url - 通信先URL
 * @param {string} [options.type="GET"] - HTTPメソッド (GET, POST, etc.)
 * @param {Object} [options.data={}] - 送信データ
 * @param {string} [options.contentType="application/x-www-form-urlencoded; charset=UTF-8"] - コンテンツタイプ
 * @param {string} [options.dataType="json"] - 期待するレスポンスデータタイプ
 * @param {function} [options.onSuccess] - 成功時のコールバック関数
 * @param {function} [options.onError] - エラー時のコールバック関数
 * 
 * @returns {jqXHR} jQueryのAjaxオブジェクト
 */
function apiRequest(options) {
    return $.ajax({
        type: options.type || "GET", // HTTPメソッドの指定、デフォルトはGET
        url: options.url, // 通信先URL
        data: options.data || {}, // 送信データ
        contentType: options.contentType,
        processData: options.processData,
        dataType: options.dataType || "json"  // 期待するレスポンスデータタイプ
    })
    // 成功時の処理
    .done(function (res) {
        console.log("【成功】", res);
        if (options.onSuccess) options.onSuccess(res);
    })
    // 失敗時の処理
    .fail(function (xhr, status, error) {
        console.error("【エラー】", status, error, xhr.responseText);
        if (options.onError) {
            options.onError(xhr, status, error);
        } else {
            alert("通信エラーが発生しました。");
        }
    })
    // 完了時の処理
    .always(function () {
        console.log("【完了】通信が完了しました。");
        if (options.onComplete) options.onComplete();
    })
    ;
}