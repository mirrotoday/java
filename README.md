<html>
  <head>
    <title>时间<title>
  </head>
      <body>
        <div style="width:100px;height:20px;">
      <script>
			   setInterval(function(){
				  var time = new Date();
				  var str=""; 
			  
			  str += time.getFullYear()+"年";
			  str += (time.getMonth()+1)+"月";
			  str += time.getDate()+"日";
			  
			  str += time.getHours()+"时";
			  str += time.getMinutes()+"分";
			  str += time.getSeconds()+"秒";
			  
			  document.body.innerHTML=str;
			   },1000);
       </script>    
    <div>  
       </body>
</html>
