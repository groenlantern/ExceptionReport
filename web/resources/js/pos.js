/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */
$( document ).ready(function() {
            
             var el = $("input:text").get(0);
            var elemLen = el.value.length;
            el.selectionStart = elemLen;
            el.selectionEnd = elemLen;
            el.focus();
            
            });
            
            
 $(function(){
  $('form').on('keypress', function(event){
    if(event.which === 13 && $(event.target).is(':input')){
        event.preventDefault();
    }
  });
});

 $(document).ready(function(){
      $('#loadbtnimg').on("click", function(){
         $('#logout').show('slow');
      });
   });
   
   function showProgress(){
       alert('showProgress');
        $("div#progressId").show();
       
   }