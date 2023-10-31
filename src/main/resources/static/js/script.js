

const toggleSidebar = () => {
	if($(".sidebar").is(":visible")){
		//if true the change display to none
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}else{
		//if true the change display to none
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","18%");
	}
};