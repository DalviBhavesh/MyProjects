

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

const search = () =>{
	
	
	let query=$("#search-input").val();
	
	if(query != ""){
		
		console.log(query);
		
		let url = `http://localhost:8080/search/${query}`;
		
		fetch(url)
		.then(response => response.json())
		.then((data) => {
			
			let text=`<div class='container text-start '>`;
			let backupText=`<div class='container text-start '>
			<div class='result'>
				<p class='lead'>No Results found........</p>
			</div>
			</div>`;
			
			data.forEach((contact)=>{
				
				text+=`<a href='/user/card/${contact.cId}' class=' link-underline link-underline-opacity-0 '>
				<div class='result'>
						
						<img class="profileImage" src='/img/${contact.image}' alt="profile.img" />
						${contact.name}
					   </div></a>
					   `
			})
			
			text+= `</div>`;
			if(data.length == 0){
				$(".search-result").html(backupText);
			}else{
				$(".search-result").html(text);
			}
			
			
		});
		
		$(".search-result").show();
		
	}else{
		$(".search-result").hide();	
	}
	
	
};