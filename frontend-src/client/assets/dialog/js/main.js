
window.alertTips=function(title,txt){

	//创建遮罩层
	var tipsLayer=document.createElement("div");
	tipsLayer.className="alertTips-layer";
	
	//创建弹窗窗口
	var alertBox=document.createElement("div");
	alertBox.className="k-widget k-window alertTips-alert-box";
	alertBox.style="padding-top: 31px; min-width: 90px; min-height: 50px; width: 550px; height: 570px; display: block; top: 87.5px; left: 565px; touch-action: pan-y; z-index: 10003;";
	
	//创建窗口里的内容
	var alertContent='<div class="k-window-titlebar k-header" style="margin-top: -31px;">&nbsp;<span class="k-window-title">'+title+'</span><div class="k-window-actions"><a  href="#" class="k-window-action k-link" onclick="doOk();"><span class="k-icon k-i-close" >close</span></a></div></div>';
	alertContent+='<div style="" class="__gys_auto_ken_window k-window-content k-content k-window-iframecontent" tabindex="0">'+txt+'</div>';
	//alertContent+='<div class="alertTips-center-box">'+txt+'</div>';
	//alertContent+='<div class="alertTips-bottom-box"><button class="bg-blue" onclick="doOk();">确定</button></div>';
	alertBox.innerHTML=alertContent;
	
	
	document.body.appendChild(tipsLayer);
	document.body.appendChild(alertBox);
	
	//隐藏提示框函数
	this.doOk=function(){
		tipsLayer.parentNode.removeChild(alertBox);
		tipsLayer.parentNode.removeChild(tipsLayer);
	}

}