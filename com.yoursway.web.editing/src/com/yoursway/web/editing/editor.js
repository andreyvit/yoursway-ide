
document.addEventListener('keyup', function(e) {
	  jDocumentChanged(document.body.innerHTML);
});

document.addEventListener('keydown', function(e) {
	  if(e.keyCode == 114){ // F3 key
		   if (document.selection && document.selection.createRange) { //IE 
		        var myRange = document.selection.createRange(); 
		        alert(myRange.startOffset); 
		        alert(myRange.endOffset); 
		    } 
		    else if (window.getSelection) { //FF, Safari, Opera 
			    var selection = window.getSelection(); 
			    var range = window.getSelection().getRangeAt(0);
			    var html = document.getElementsByTagName('html')[0];
			    //alert(.innerHTML); 
		        //alert(range.startOffset); 
		        //alert(range.endOffset);
		        var magic = document.createTextNode("§");
		        range.insertNode(magic);
		        var pos = html.innerHTML.indexOf("§");
		        magic.parentNode.removeChild(magic);
				jGoToSource(pos);
			}
	  }
});

document.body.contentEditable='true';document.designMode='on';
