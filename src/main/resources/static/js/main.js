
/**
 * ログアウト
 * CSRFトークンを付与してPOST送信する
 */
function postLogout() {
	// 
	const token = $('meta[name="_csrf"]').attr('content');
	const paramName = $('meta[name="_csrf_parameter"]').attr('content');

	// HTMLにformを書かずに、動的に作ってsubmitする
	const $form = $('<form>', { id: '__logoutForm', method: 'POST', action: '/logout' });

	// 既存のフォームがあれば削除（連打対策）
	$('#__logoutForm').remove();

	if(token && paramName) {
		$('<input>', { type: 'hidden', name: paramName, value: token }).appendTo($form);
	}


	// DOMに追加して submit
	$form.appendTo('body');
	$form[0].submit();
}