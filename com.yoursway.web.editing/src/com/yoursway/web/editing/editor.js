function nodePath(node) {
  var output = "";
  while(node){
    output += "/"+node.name+'['+node.childIndex+']'
    node = node.parentNode;
  }
  return output;
}

document.addEventListener('keyup', function(e) {
	  jDocumentChanged(printTree(document.body));
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
				  jGoToSource(nodePath(selection.anchorNode), selection.anchorOffset);
			}
	  }
});

jDocumentCreated(document);

document.body.contentEditable='true';document.designMode='on';
