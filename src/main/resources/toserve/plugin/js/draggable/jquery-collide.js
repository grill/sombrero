$.ui.plugin.add("draggable", "collide", {
    start: function(event, ui) {
        var $t = $(this), widget = $t.data("draggable");
        // TODO - make peers configurable
        widget.peers = $t.siblings();
        widget.mode = widget.options.collide;
        // If we're being dragged, then *we're* the overlapper. :)
        if (widget.mode == 'flag') $t.removeClass('ui-draggable-overlapped');
    },

    drag: function(event, ui) {
        var $t = $(this), widget = $t.data("draggable");
        if (widget.mode == 'flag') $t.removeClass('ui-draggable-overlapping');

       $(widget.peers).each(function (i, peer) {
            var $p = $(peer);
            var ix1 = ui.position.left, ix2 = ix1 + $t.width();
            var iy1 = ui.position.top, iy2 = iy1 + $t.height();
            var px1 = $p.position().left, px2 = px1 + $p.width();
            var py1 = $p.position().top, py2 = py1 + $p.height();
            
            // Test for overlap
            if (((ix1 <= px1 && ix2 >= px1) || (ix1 <= px2 && ix2 >= px2)) &&
                ((iy1 <= py1 && iy2 >= py1) || (iy1 <= py2 && iy2 >= py2))) {
                if (widget.mode == 'flag') {
                    $p.addClass('ui-draggable-overlapped');
                    $t.addClass('ui-draggable-overlapping');
                    $p.removeClass('ui-draggable-overlapping');
                } else if (widget.mode == 'block') {
                	var left = ui.position.left;
                	var top = ui.position.top;
                	var that = $(this);
                	var hel;
                	
                	var contains = function(t, l){
                		var container = $t.draggable('option', 'containment');
                		var pos = container.offset();
                		
                		return (t >= pos.top && l >= pos.left &&
                				t+$t.height() <= pos.top+container.height() &&//wenn alle passen true
                				l+$t.width() <= pos.left+container.width());
                	};
                	var collision = function(t, l){
                		var ret = false;
                		
                		$(widget.peers).each(function (i, peer) {
                            var $p = $(peer);
                            var px1 = $p.position().left, px2 = px1 + $p.width();
                            var py1 = $p.position().top, py2 = py1 + $p.height();
                            
                			ret = ret || (((l <= px1 && l+$t.width() >= px1) || (l <= px2 && l+$t.width() >= px2)) &&
                                    ((t <= py1 && t+$t.height() >= py1) || (t <= py2 && t+$t.height() >= py2)));
                		});
                		return ret;
                		/*
                		t >= py1 || l >= px1 || 
                				t+$t.height() <= py2 ||
                        		l+$t.width() <= px2;*/
                	};
                    // Calculate a ratio of the overlap to decide which face to slide along
                    overlapx = Math.min(ix2, px2) - Math.max(ix1, px1);
                    overlapy = Math.min(iy2, py2) - Math.max(iy1, py1);
                    if (Math.abs(overlapx) > Math.abs(overlapy)) {
                        if (iy1 <= py1) hel = (py1 - $t.height())-1;
                        if (iy1 > py1) hel = (py2)+1;
                        if(!contains(hel, $t.offset().left)){
                        	$("#ausg").append("<div>1 " + widget.oldTop + " " + widget.oldLeft + "</div>");
                            /*if(!contains(hel, $t.offset().left)){
                            	$("#ausg").append("<div>1 " + hel + "</div>");
                            	//alert("1");
                                if (iy1 <= py1) hel = (py2)+1;
                                if (iy1 > py1) hel = (py1 - $t.height())-1;
                            }
                        	$("#ausg").append("<div>1 " + hel + "</div>");
                        	ui.position.left = left;
                        	ui.position.top = hel;*/
                        	if(collision(widget.oldTop, ui.position.left)){
                            	$("#ausg").append("<div>1!! " + widget.oldLeft + "</div>");
                        		ui.position.left = widget.oldLeft;
                        	}else
                            	widget.oldLeft = ui.position.left;
                        	ui.position.top = widget.oldTop;
                        	//alert("4");
                        }else{
                        	$("#ausg").append("<div>2 " + top + " " + left + "</div>");
                        	//alert("3");
                        	//$("#ausg").append("<div>2 " + hel + "</div>");
                        	if(collision(hel, ui.position.left)){
                        		ui.position.left = widget.oldLeft;
                        		ui.position.top = widget.oldTop;
                        	}else{
                            	widget.oldLeft = ui.position.left;
                            	widget.oldTop = hel;
                            	ui.position.top = hel;
                        	}
                        }
                    } else {
                        if (ix1 <= px1) hel = (px1 - $t.width())-1;
                        if (ix1 > px1) hel = (px2)+1;
                        if(!contains($t.offset().top, hel)){
                        	$("#ausg").append("<div>3 " + top + " " + left + "</div>");
                            /*if(!contains($t.offset().top, hel)){
                            	$("#ausg").append("<div>3 " + hel + " " + top + "</div>");
                            	//alert("3");
                                if (ix1 <= px1) hel = (px1 - $t.width())-1;
                                if (ix1 > px1) hel = (px2)+1;
                            }
                            if(!contains($t.offset().top, hel)){
                            	$("#ausg").append("<div>3 " + hel + " " + top + "</div>");
                            	//alert("3");
                            	if (ix1 <= px1) hel = (px2)+1;
                            	if (ix1 > px1) hel = (px1 - $t.width())-1;
                            }
                        	$("#ausg").append("<div>3 " + hel + "</div>");
                        	ui.position.left = hel;
                        	ui.position.top = top;*/
                        	if(collision(ui.position.top, widget.oldLeft))
                        		ui.position.top = widget.oldTop;
                        	else
                            	widget.oldTop = ui.position.top;
                        	ui.position.left = widget.oldLeft;
                        }else{
                        	$("#ausg").append("<div>4 " + top + " " + left + "</div>");
                            /*if(!contains($t.offset().top, hel)){
                            	$("#ausg").append("<div>4</div>");
                            	//alert("4");
                                if (ix1 <= px1) hel = (px1 - $t.width())-1;
                                if (ix1 > px1) hel = (px2)+1;
                            }
                        	$("#ausg").append("<div>4 " + hel + "</div>");
                        	ui.position.left = hel;*/
                        	if(collision(ui.position.top, hel)){
                        		ui.position.left = widget.oldLeft;
                        		ui.position.top = widget.oldTop;
                        	}else{
                            	widget.oldTop = ui.position.top;
                            	widget.oldLeft = hel;
                            	ui.position.left = hel;
                        	}
                        }
                    }
                }
            } else {
                if (widget.mode == 'flag') {
                    $p.removeClass('ui-draggable-overlapping');
                    $p.removeClass('ui-draggable-overlapped');
                }
            }
        });
    }
});
