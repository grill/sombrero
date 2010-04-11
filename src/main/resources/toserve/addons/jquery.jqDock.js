/*
 * jqDock jQuery plugin
 * Version : 1.2
 * Author : Roger Barrett
 * Date : June 2008
 *
 * Inspired by:
 *   iconDock jQuery plugin
 *   http://icon.cat/software/iconDock
 *   version: 0.8 beta
 *   date: 2/05/2007
 *   Copyright (c) 2007 Isaac Roca & icon.cat (iroca@icon.cat)
 *   Dual licensed under the MIT-LICENSE.txt and GPL-LICENSE.txt
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Dual licensed under the MIT-LICENSE.txt and GPL-LICENSE.txt
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 * Change Log :
 * v1.2
 *    - Fixes for Opera v9.5 - many thanks to Rubel Mujica
 * v1.1
 *    - some speed optimisation within the functions called by the event handler
 *    - added positioning of labels (top/middle/bottom and left/center/right)
 *    - added click handler to label (triggers click event on related image)
 *    - added jqDockLabel(Link|Image) class to label, depending on type of current image
 *    - updated demo and documentation for label positioning and clicking on labels
 */
;
(function($){
if(!$.fn.jqDock){ //can't see why it should be, but it doesn't hurt to check

  var jqDock = function(){
    //return an object...
    return {
        version : 1.2
      , defaults : { //can be set at runtime, per menu
          size : 36 //[px] maximum minor axis dimension of image (width or height depending on 'align' : vertical menu = width, horizontal = height)
        , distance : 54 //[px] attenuation distance from cursor
        , coefficient : 1.5 //attenuation coefficient
        , duration : 500 //[ms] duration of initial expansion and off-menu shrinkage
        , align : 'bottom' //[top/middle/bottom or left/center/right] fixes horizontal/vertical expansion axis
        , labels : false //enable/disable display of a label on the current image
        , source : false //function: given context of relevant image element; passed index of image within menu; required to return image source path, or false to use original
        , loader : null //overrides useJqLoader if set to 'image' or 'jquery'
        }
      , useJqLoader : $.browser.opera || $.browser.safari //use jQuery method for loading images, rather than "new Image()" method
      , shrinkInterval : 100 //(ms) the timer interval between each step of the off-menu shrinking
      , docks : [] //array of dock menus
      , X : 0 //mouse position from left
      , Y : 0 //mouse position from top
      //internals to cut down code and ease decision-making (mainly between vertical and horizontal menus)...
      , verthorz : { v: { wh:'height', xy:'Y', tl:'top', lead:'Top', trail:'Bottom', act:'ActualInv' } //Opts.align = left/center/right
                   , h: { wh:'width', xy:'X', tl:'left', lead:'Left', trail:'Right', act:'Actual' } //Opts.align = top/middle/bottom
                   }
      , elementCss : { position:'relative', borderWidth:0, borderStyle:'none', verticalAlign:'top' }
      , vanillaDiv : '<div style="position:relative;margin:0px;padding:0px;border:0px none;background-color:transparent;">'

  /* initDock()
  *  ==========
  * called by the image onload function, it stores and sets image height/width;
  * once all images have been loaded, it completes the setup of the dock menu
  * note: unless all images get loaded, the menu will stay hidden!
  * @context jqDock
  * @param integer (dock index)
  */
      , initDock : function(id){
      //========================================
          var ME = this
            , Dock = this.docks[id] //convenience
            , op = Dock.Opts //convenience
            , off = 0
            , AI = $('a, img', Dock.Menu)
            , i = 0
            , j, el, wh, acc, upad
            , opPre95 = ($.browser.opera && (1*($.browser.version.match(/^(\d+\.\d+)/)||[0,0])[1]) < 9.5) // v1.2 : need to distinguish Opera v9.5
            ;
          // things will screw up if we don't clear text nodes...
          this.removeText(Dock.Menu);
          //set some basic styles on the dock elements, otherwise it won't work
          if(op.orient.vh == 'h'){
            AI.css(this.elementCss);
            if(opPre95 || !$.boxModel){ //Opera (v1.2 : pre v9.5 only), and IE in quirks mode, can't handle floated blocks...
              AI.filter('a').css({lineHeight:0, fontSize:'0px'});
            }else{ //not Opera or IE in quirks mode...
              var hcss = {display:'block'};
              hcss['float'] = 'left'; //don't want any 'reserved word' problems from IE
              AI.filter('img').css(hcss);
            }
          }else{ //vertical docks require a div wrapper around each menu element (v1.2 : set anchors/images to display block)...
            AI.not($('a img', Dock.Menu)).wrap(this.vanillaDiv + '</div>').end().css(this.elementCss).css({display:'block'});
          }
          //resize each image and store various settings wrt main axis...
          while(i < Dock.Elem.length){
            el = Dock.Elem[i++];
            //resize the image to make the minor axis dimension meet the specified 'Opts.size'...
            wh = this.keepProportion(el, op.size, {vh:op.orient.inv, inv:op.orient.vh}); //inverted!
            el.Actual = el.Final = el.Initial = wh[op.vh.wh];
            el.SizeDiff = el[op.vh.wh] - el.Initial; //on main axis!
            el.Img.css(wh); //resize the image to its new shrunken setting
            //remove titles, alt text...
            el.Img.removeAttr('title').attr({alt:''}).parent('a').removeAttr('title');
            //calculate shrinkage step size
            el.ShrinkStep = Math.floor(el.SizeDiff * this.shrinkInterval / op.duration);
            //use inverts because we're after the minor axis dimension...
            Dock[op.vh.inv.wh] = Math.max(Dock[op.vh.inv.wh], op.size + el.Pad[op.vh.inv.lead] + el.Pad[op.vh.inv.trail]);

            el.Offset = off;
            el.Centre = el.Offset + el.Pad[op.vh.lead] + (el.Initial / 2);
            off += el.Initial + el.Pad[op.vh.lead] + el.Pad[op.vh.trail];
          }

          //'best guess' at calculating max 'spread' (main axis dimension - horizontal or vertical) of menu:
          //for each img element of the menu, call setSizes() with a forced cursor position of the centre of the image;
          //setSizes() will set each element's Final value, so tally them all, including user-applied padding, to give
          //an overall width/height for this cursor position; set dock width/height to be the largest width/height found
          i = 0;
          while(i < Dock.Elem.length){
            el = Dock.Elem[i++];
            acc = 0; //accumulator for main axis image dimensions
            upad = el.Pad[op.vh.lead] + el.Pad[op.vh.trail]; //user padding in main axis
            //tally the minimum widths...
            Dock.Spread += el.Initial + upad;
            //set sizes with an overridden cursor position...
            this.setSizes(id, el.Centre);
            //tally image widths/heights (plus padding)...
            j = Dock.Elem.length;
            while(j){
              //note that Final is an image dimension (in main axis) and does not include any user padding...
              acc += Dock.Elem[--j].Final + upad;
            }
            //keep largest main axis dock dimension...
            Dock[op.vh.wh] = Math.max(Dock[op.vh.wh], acc);
          }
          //reset Final for each image...
          while(i){
            el = Dock.Elem[--i];
            el.Final = el.Initial;
          }
          var wrap = [ this.vanillaDiv
                     , '<div class="jqDock" style="position:absolute;top:0px;left:0px;padding:0px;'
                     , 'margin:0px;overflow:visible;height:'
                     , Dock.height, 'px;width:', Dock.width
                     , 'px;"></div></div>'
                     ].join('');
          Dock.Yard = $(Dock.Menu).wrapInner(wrap).find('div.jqDock');
          //let's see if the user has applied any css border styling to div.jqDock...
          $.each([op.vh.lead, op.vh.trail], function(n, v){
              Dock.Borders[v] = ME.asNumber(Dock.Yard.css('border'+v+'Width'));
            });
          //if div.jqDock has a border we need to shift it a bit so the border doesn't get lost...
          if(Dock.Borders[op.vh.lead]){
            Dock.Yard.css(op.vh.tl, Math.ceil(Dock.Borders[op.vh.lead] / 2));
          }
          //shrink all images down to 'at rest' size, and add appropriate identifying class...
          while(i < Dock.Elem.length){
            el = Dock.Elem[i];
            this.changeSize(id, i, el.Final, true); //force
            el.Img.addClass('jqDockMouse'+id+'_'+(i++));
          }
          //show the menu now...
          $(Dock.Menu).show();
          //now that the menu is visible we can set up labels and get label widths...
          if(Dock.Opts.labels){
            $.each(Dock.Elem, function(i){
                ME.setLabel(id, this.Label);
              });
            Dock.Label.hide();
          }
          //bind a mousehandler to the menu...
          Dock.Yard.bind('mouseover mouseout mousemove', function(e){ ME.mouseHandler(e); });
        } //end function initDock()

  /* altImage()
  *  ==========
  * tests to see if an image has an alt attribute that looks like an image path, returning it if found, else false
  * note: context of the image element
  * @context DOM element (image)
  * @return string (image path) or false
  */
      , altImage : function(){
          var alt = $(this).attr('alt');
          return (alt && alt.match(/\.(gif|jpg|jpeg|png)$/i)) ? alt : false;

        } //end function altImage()

  /* removeText()
  *  ============
  * removes ALL text nodes from the menu, so that we don't get spacing issues between menu elements
  * note : this includes text within anchors
  * @context jqDock
  * @param DOM element
  * @recursive
  */
      , removeText : function(el){
      //==========================
          var i = el.childNodes.length
            , j
            ;
          while(i){
            j = el.childNodes[--i];
            if(j.childNodes && j.childNodes.length){
              this.removeText(j);
            }else if(j.nodeType == 3){
              el.removeChild(j);
            }
          }
        } //end function removeText()

  /* asNumber()
  *  ==========
  * returns numeric of leading digits in string argument
  * @context jqDock
  * @param string
  * @return integer
  */
      , asNumber : function(x){
      //=========================
          var r = parseInt(x, 10);
          return isNaN(r) ? 0 : r;
        } //end function asNumber()

  /* keepProportion()
  *  ================
  * returns an object containing width and height, with the one NOT represented by 'dim'
  * being calculated proportionately
  * if horizontal then attenuation is along vertical (x) axis, thereby setting the new
  * dimension for width, so the one to keep in proportion is height; and vice versa for
  * vertical menus, obviously!
  * @context jqDock
  * @param object (element of elements array)
  * @param integer (image dimension)
  * @param object (dock orientation)
  * @return integer (other image dimension)
  */
      , keepProportion : function(el, dim, orient){
      //===========================================
          var r = {}
            , vh = this.verthorz[orient.vh] //convenience
            , inv = this.verthorz[orient.inv] //convenience
            ;
          r[vh.wh] = dim;
          r[inv.wh] = Math.round(dim * el[inv.wh] / el[vh.wh]);
          return r;
        } //end function keepProportion()

  /* deltaXY()
  *  =========
  * translates this.X or this.Y into an offset within div.jqDock
  * note: doing it this way means that all attenuation is against the inital (shrunken) image positions,
  * but it saves having to find every image's offset() each time the cursor moves or an image changes size!
  * @context jqDock
  * @param integer (dock index)
  */
      , deltaXY : function(id){
      //=======================
          var Dock = this.docks[id]; //convenience
          if(Dock.Current !== false){
            var op = Dock.Opts //convenience
              , el = Dock.Elem[Dock.Current] //convenience
              , p = el.Pad[op.vh.lead] + el.Pad[op.vh.trail] //element's user-specified padding
              , off = el.Img.offset()
              ;
            //get the difference between the cursor position and the leading edge of the current image,
            //multiply by the full/shrunken ratio, and add the element's pre-calculated offset within div.jqDock...
            Dock.Delta = Math.floor((this[op.vh.xy] - off[op.vh.tl]) * (p + el.Initial) / (p + el.Actual)) + el.Offset;
            this.doLabel(id, off);
          }
        } //end function deltaXY()

  /* setLabel()
  *  ==========
  * sets up the labels, storing each image's label dimensions
  * @context jqDock
  * @param integer (dock index)
  * @param object (menu element's label settings)
  */
      , setLabel : function(id, label){
      //===============================
          var Dock = this.docks[id] //convenience
            , ME = this
            , pad = {}
            ;
          if(!Dock.Label){ //create the div.jqDockLabel and hide it...
            Dock.Label = $('<div class="jqDockLabel jqDockMouse'+id+'_00 jqDockLabelImage" style="position:absolute;margin:0px;"></div>')
                           .hide().bind('click', function(){ Dock.Elem[Dock.Current].Img.trigger('click'); }).appendTo(Dock.Yard);
          }
          if(label.txt){
            //insert the label text for this image, and find any user-styled padding...
            Dock.Label.text(label.txt);
            $.each(['Top', 'Right', 'Bottom', 'Left'], function(n, v){
                pad[v] = ME.asNumber(Dock.Label.css('padding'+v));
              });
            //store the label dimensions for this image...
            $.each(this.verthorz, function(vh, o){
                label[o.wh] = Dock.Label[o.wh]();
                label[o.wh+'Pad'] = pad[o.lead] + pad[o.trail]; //hold padding separately
              });
          }
        } //end function setLabel()

  /* doLabel()
  *  =========
  * if labels enabled, performs the appropriate action
  * @context jqDock
  * @param integer (dock index)
  * @param string (what action to do) or object (top/left offset of an image)
  */
      , doLabel : function(id, off){
      //============================
          var Dock = this.docks[id]; //convenience
          if(Dock.Opts.labels && Dock.Current !== false){ //only if labels are set and we're over an image
            var el = Dock.Elem[Dock.Current] //convenience
              , L = el.Label //convenience
              , op = Dock.Opts //convenience
              , what = typeof off == 'string' ? off : 'move'
              ;
            switch(what){
              case 'show': case 'hide' : //show or hide...
                Dock.Label[L.txt?what:'hide']();
                break;
              case 'change': //change the label text and set the appropriate dimensions for the current image...
                Dock.Label[0].className = Dock.Label[0].className.replace(/(jqDockLabel)(Link|Image)/, '$1'+(el.Linked ? 'Link' : 'Image'));
                Dock.Label.text(L.txt).css({width:L.width, height:L.height}).hide();
                break;
              default: //move the label...
                //can't avoid extra processing here because we have to get the dock's offsets realtime since simply
                //expanding/shrinking a dock can make scroll bars appear/disappear and thereby affect the dock's position
                var doff = Dock.Yard.offset()
                  , css = { top: off.top - doff.top
                          , left: off.left - doff.left
                          }
                  , splt = op.labels.split('')
                  ;
                //note: if vertically or horizontally centred then centre is based on the IMAGE only
                //(ie without including padding), otherwise, positioning includes anyimage padding
                if(splt[0] == 'm'){
                  css.top += Math.floor((el[op.vh.inv.act] - L.height - L.heightPad) / 2);
                }else if(splt[0] == 'b'){
                  css.top += el[op.vh.inv.act] + el.Pad.Top + el.Pad.Bottom - L.height - L.heightPad;
                }
                if(splt[1] == 'c'){
                  css.left += Math.floor((el[op.vh.act] - L.width - L.widthPad) / 2);
                }else if(splt[1] == 'r'){
                  css.left += el[op.vh.act] + el.Pad.Left + el.Pad.Right - L.width - L.widthPad;
                }
                Dock.Label.css(css);
            }
          }
        } //end function doLabel()

  /* mouseHandler()
  *  ==============
  * handler for all bound mouse events (move/over/out)
  * note: this handles both image and label events
  * note: when moving within a label Opera reports both a mousemove and a mouseover (presumably because the label has been moved?), but the mouseover does not have a relatedTarget!
  * @context jqDock
  * @param object (event)
  * @return null or false
  */
      , mouseHandler : function(e){
      //===========================
          var r = null
            , t = e.target.className.match(/jqDockMouse(\d+)_(\d+)/)
              //on a mouseout from an image onto a label, Opera reports relatedTarget as existing, but with tagName and className as 'undefined'!...
            , rt = !!(e.relatedTarget) && e.relatedTarget.tagName !== undefined
            ;
          if(t){
            r = false; //prevent the event going any further
            var id = 1*t[1] //convenience
              , Dock = this.docks[id] //convenience
              , idx = t[2] == '00' ? Dock.Current : 1*t[2] //note: label events have _00 suffix on the class name
              ;
            this.X = e.pageX;
            this.Y = e.pageY;
            if(e.type == 'mousemove'){
              if(idx == Dock.Current){ //precedence to mouseover/out processing...
                this.deltaXY(id);
                if(Dock.OnDock && Dock.Expanded){
                  this.setSizes(id);
                  this.factorSizes(id);
                }
              }
            }else{
              var rel = rt && e.relatedTarget.className.match(/jqDockMouse(\d+)_(\d+)/);
              //only do something on a mouseover if the current menu element has changed...
              if(e.type == 'mouseover' && (!Dock.OnDock || idx !== Dock.Current)){
                Dock.Current = idx;
                this.doLabel(id, 'change');
                this.deltaXY(id);
                if(Dock.Expanded){
                  this.doLabel(id, 'show');
                }
                if(rt && (!rel || rel[1] != id)){ //came from outside this menu...
                  Dock.Timestamp = (new Date()).getTime();
                  this.setSizes(id);
                  Dock.OnDock = true;
                  this.overDock(id); //sets Expanded when complete
                }
              //only do something on a mouseout if we can tell where we are mousing out to...
              }else if(rt && e.type == 'mouseout'){
                if(!rel || rel[1] != id){ //going outside this menu...
                  Dock.OnDock = false;
                  this.doLabel(id, 'hide');
                  //reset Final dims, per element, to the original (shrunken)...
                  var i = Dock.Elem.length;
                  while((i--)){
                    Dock.Elem[i].Final = Dock.Elem[i].Intial;
                  }
                  this.offDock(id); //clears Expanded and Current when complete
                }
              }
            }
          }
          return r;
        } //end function mouseHandler()

  /* overDock()
  *  ==========
  * checks for completed expansion (if OnDock)
  * if not completed, runs setSizes(), factorSizes(), and then itself on a 60ms timer
  * @context jqDock
  * @param integer (dock index)
  */
      , overDock : function(id){
      //========================
          var Dock = this.docks[id]; //convenience
          if(Dock.OnDock){
            var ME = this
              , el = Dock.Elem //convenience
              , i = el.length
              ;
            while((i--) && !(el[i].Actual < el[i].Final)){}
            if(i < 0){ //complete
              Dock.Expanded = true;
              this.deltaXY(id);
              this.doLabel(id, 'show');
            }else{
              this.setSizes(id);
              this.factorSizes(id);
              setTimeout(function(){ ME.overDock(id); }, 60);
            }
          }
        } //end function overDock()

  /* offDock()
  *  =========
  * called when cursor goes outside menu, and checks for completed shrinking of all menu elements
  * calls changeSize() on any menu element that has not finished shrinking
  * calls itself on a timer to complete the shrinkage
  * @context jqDock
  * @param integer (dock index)
  */
      , offDock : function(id){
      //=======================
          var Dock = this.docks[id]; //convenience
          if(!Dock.OnDock){
            var ME = this
              , done = true
              , i = Dock.Elem.length
              , el, sz
              ;
            while(i){
              el = Dock.Elem[--i];
              if(el.Actual > el.Initial){
                sz = el.Actual - el.ShrinkStep;
                if(sz > el.Initial){
                  done = false;
                }else{
                  sz = el.Initial;
                }
                this.changeSize(id, i, sz);
              }
            }
            //this is here for no other reason than that Opera leaves a 'shadow' residue of the expanded image unless/until Delta is recalculated!...
            this.deltaXY(id);
            if(done){
              //reset everything back to 'at rest' state...
              while(i < Dock.Elem.length){
                el = Dock.Elem[i++];
                el.Actual = el.Final = el.Initial;
              }
              Dock.Current = Dock.Expanded = false;
            }else{
              setTimeout(function(){ ME.offDock(id); }, this.shrinkInterval);
            }
          }
        } //end function offDock()

  /* setSizes()
  *  ==========
  * calculates the image sizes according to the current (translated) position of the cursor within div.jqDock
  * result stored in Final for each menu element
  * @context jqDock
  * @param integer (dock index)
  * @param integer (translated cursor offset in main axis)
  */
      , setSizes : function(id, mxy){
      //=============================
          var Dock = this.docks[id] //convenience
            , op = Dock.Opts //convenience
            , i = Dock.Elem.length
            , el, sz
            ;
          mxy = mxy || Dock.Delta; //if not forced, use current translated cursor position (main axis)
          while(i){
            el = Dock.Elem[--i];
            //if we're within the attenuation distance then sz will be less than the difference between the max and min dims
            //if we're smack on or beyond the attenuation distance then set to the min dim
            //note: set sz to an integer number, otherwise we could end up 'fluttering'
            sz = Math.floor(el.SizeDiff * Math.pow(Math.abs(mxy - el.Centre), op.coefficient) / op.attenuation);
            el.Final = (sz < el.SizeDiff ? el[op.vh.wh] - sz : el.Initial);
          }
        } //end function setSizes()

  /* factorSizes()
  *  =============
  * modifies the target sizes in proportion to 'duration' if still within the 'duration' period following a mouseover
  * calls changeSize() for each menu element (if more than 60ms since mouseover)
  * @context jqDock
  * @param integer (dock index)
  */
      , factorSizes : function(id){
      //===========================
          var Dock = this.docks[id] //convenience
            , op = Dock.Opts //convenience
            , lapse = op.duration + 60
            ;
          if(Dock.Timestamp){
            lapse = (new Date()).getTime() - Dock.Timestamp;
            //Timestamp only gets set on mouseover (onto menu) so there's no point continually checking Date once op.duration has passed...
            if(lapse >= op.duration){
              Dock.Timestamp = 0;
            }
          }
          if(lapse > 60){ //only if more than 60ms have passed since last mouseover
            var f = lapse < op.duration ? lapse / op.duration : 0
              , i = 0 //must go through the elements if logical order
              , el
              ;
            while(i < Dock.Elem.length){
              el = Dock.Elem[i];
              this.changeSize(id, i++, (f ? Math.floor(el.Initial + ((el.Final - el.Initial) * f)) : el.Final));
            }
          }
        } //end function factorSizes()

  /* changeSize()
  *  ============
  * sets the css for an individual image to effect its change in size
  * 'dim' is the new value for the main axis dimension as specified in Opts.vh.wh, so
  * the margin needs to be applied to the inverse of Opts.vh.wh!
  * note: 'force' is only set when called from initDock() to do the initial shrink
  * @context jqDock
  * @param integer (dock index)
  * @param integer (image index)
  * @param integer (main axis dimension of image)
  * @param boolean
  */
      , changeSize : function(id, idx, dim, force){
      //===========================================
          var Dock = this.docks[id] //convenience
            , el = Dock.Elem[idx] //convenience
            ;
          if(force || el.Actual != dim){
            var op = Dock.Opts //convenience
              //vertical menus, or IE in quirks mode, require border widths (if any) of the Dock to be added to the Dock's main axis dimension...
              , bdr = ($.boxModel || op.orient.vh == 'v') ? 0 : Dock.Borders[op.vh.lead] + Dock.Borders[op.vh.trail]
              ;
            //switch image source to large, if (a) it's different to small source, and (b) this is the first step of an expansion...
            if(el.Source[2] && !force && el.Actual == el.Initial){
              el.Img[0].src = el.Source[1];
            }
            if(Dock.OnDock){
              this.deltaXY(id); //recalculate deltaXY
            }
            Dock.Spread += dim - el.Actual; //adjust main axis dimension of dock
            var css = this.keepProportion(el, dim, op.orient)
              , diff = op.size - css[op.vh.inv.wh]
              , m = 'margin' //convenience
              , z = op.vh.inv //convenience
              ;
            //add minor axis margins according to alignment...
            //note: where diff is an odd number of pixels, for 'middle' or 'center' alignment put the odd pixel in the 'lead' margin
            switch(op.align){
              case 'bottom': case 'right' : css[m+z.lead] = diff; break;
              case 'middle': case 'center' : css[m+z.lead] = (diff + diff%2) / 2; css[m+z.trail] = (diff - diff%2) / 2; break;
              case 'top': case 'left': css[m+z.trail] = diff; break;
              default:
            }
            //set dock's main axis dimension...
            Dock.Yard[op.vh.wh](Dock.Spread + bdr);
            //change image size and margins...
            el.Img.css(css);
            //set dock's main axis 'lead' margin (v1.2: make sure that margin doesn't go negative!)...
            Dock.Yard.css('margin'+op.vh.lead, Math.floor(Math.max(0, (Dock[op.vh.wh] - Dock.Spread) / 2)));
            //store new dimensions...
            el.Actual = dim; //main axis
            el.ActualInv = css[op.vh.inv.wh]; //minor axis
            //switch image source to small, if (a) it's different to large source, and (b) this was the last step of a shrink...
            if(el.Source[2] && !force && el.Actual == el.Initial){
              el.Img[0].src = el.Source[0];
            }
          }
        } //end function changeSize()
      }; //end of return object
    }(); //run the function to set up jqDock

  /***************************************************************************************************
  *  jQuery.fn.jqDock()
  *  ==================
  * usage:    $(selector).jqDock(options);
  * options:  see jqDock.defaults (top of script)
  *
  * note: the aim is to do as little processing as possible after setup, because everything is
  * driven from the mousemove/over/out events and I don't want to kill the browser if I can help it!
  * hence the code below, and in jqDock.initDock(), sets up and stores everything it possibly can
  * which will avoid extra processing at runtime, and hopefully give as smooth animation as possible.
  ***************************************************************************************************/
  $.fn.jqDock = function(opts){
    return this.filter(function(){
        //check not already set up and has images...
        var i = jqDock.docks.length;
        while((i--) && this != jqDock.docks[i].Menu){}
        return (i < 0) && ($('img', this).length);
      }).hide() //hide it/them
      .each(function(){
        //add an object to the docks array for this new dock...
        var id = jqDock.docks.length;
        jqDock.docks[id] = { Elem : [] // an object per img menu option
                           , Menu : this //original containing element
                           , OnDock : false //indicates cursor over menu and initial sizes set
                           , Expanded : false //indicates completion of initial menu element expansions
                           , Timestamp : 0 //set on mouseover and used (within opts.duration) to proportion the menu element sizes
                           , width : 0 //width of div.jqDock container
                           , height : 0 //height of div.jqDock container
                           , Spread : 0 //main axis dimension (horizontal = width, vertical = height)
                           , Borders : {} //border widths (main axis) on div.jqDock
                           , Yard : false //jQuery of div.jqDock
                           , Opts : $.extend({}, jqDock.defaults, opts||{}) //options
                           , Current : false //current image index
                           , Delta : 0 //X or Y translated into horizontal or vertical offset within div.jqDock as if all images were unexpanded
                           , Loaded : 0 //count of images loaded
                           , Label : false //jQuery of label container (if Opts.labels is set)
                           };
        var Dock = jqDock.docks[id] //convenience
          , op = Dock.Opts //convenience
          ;
        //set up some extra Opts now, just to save some computing power later...
        op.attenuation = Math.pow(op.distance, op.coefficient); //straightforward, static calculation
        op.orient = ({left:1, center:1, right:1}[op.align]) ? {vh:'v', inv:'h'} : {vh:'h', inv:'v'}; //orientation based on 'align' option
        op.vh = $.extend({}, jqDock.verthorz[op.orient.vh], {inv:jqDock.verthorz[op.orient.inv]}); //main and minor axis internals
        op.loader = (op.loader) && typeof op.loader == 'string' && /^image|jquery$/i.test(op.loader) ? op.loader.toLowerCase() : ''; //image loader override
        op.labels = op.labels === true ? {top:'bc',left:'tr',right:'tl'}[op.align] || 'tc' : (typeof op.labels == 'string' && {tl:1,tc:1,tr:1,ml:1,mc:1,mr:1,bl:1,bc:1,br:1}[op.labels] ? op.labels : false);

        $('img', this).each(function(n){
            //add an object to the dock's elements array for each image...
            var me = $(this)
              , s0 = me.attr('src') //'small' image source
              , s1 = (op.source ? op.source.call(me[0], n) : false) || jqDock.altImage.call(this) || s0 //'large' image source?
              , tx = op.labels ? me.attr('title') || me.parent('a').attr('title') || '' : '' //label text?
              ;
            Dock.Elem[n] = { Img : me //jQuery of img element
                           , Source : [ s0, s1, !(s0 == s1) ] //array : [ small image path, large image path, different? ]
                           , Label : { txt: tx, width: 0, height: 0, widthPad: 0, heightPad: 0 } //label text, dimensions, user-applied padding
                           , Initial : 0 //width/height when fully shrunk; it's important to note that this is not necessarily the same as Opts.size!
                           , Actual : 0 //transitory width/height (main axis)
                           , ActualInv : 0 //transitory width/height (minor axis)
                           , Final : 0 //target width/height
                           , Offset : 0 //offset of 'lead' edge of the image within div.jqDock (including user-padding)
                           , Centre : 0 //'Offset' + 'lead' user-padding + half 'Initial' dimension
                           , Pad : {} //user-applied padding, set up below
                           , Linked : !!me.parent('a').length //image-within-link or not
                           , width : 0 //original width of img element (the one that expands)
                           , height : 0 //original height of img element (the one that expands)
                           };
            $.each(['Top', 'Right', 'Bottom', 'Left'], function(i, v){
                Dock.Elem[n].Pad[v] = jqDock.asNumber(me.css('padding'+v));
              });
          });
        //we have to run a 'loader' function for the images because the expanding image
        //may not be part of the current DOM. what this means though, is that if you
        //have a missing image in your dock, the entire dock will not be displayed!
        //however I've had a few problems with certain browsers: for instance, IE does
        //not like the jQuery method; and Opera was causing me problems with the native
        //method when reloading the page; I've also heard rumours that Safari 2 might cope better with
        //the jQuery method, but I cannot confirm since I no longer have Safari 2.
        //
        //anyway, I'm providing both methods. if anyone finds it doesn't work, try
        //overriding with option.loader, and/or changing jqDock.useJqLoader for the 
        //browser in question and let me know if that solves it.
        var jqld = (!op.loader && jqDock.useJqLoader) || op.loader == 'jquery';
        $.each(Dock.Elem, function(i){
            var me = this
              , iLoaded = function(){
                  //store 'large' width and height...
                  me.height = this.height;
                  me.width = this.width;
                  if(++Dock.Loaded >= Dock.Elem.length){ //check to see if all images are loaded...
                    setTimeout(function(){ jqDock.initDock(id); }, 0);
                  }
                }
              ;
            if(jqld){ //jQuery method...
              $('<img />').bind('load', iLoaded).attr({src:this.Source[1]});
            }else{ //native 'new Image()' method...
              var pre = new Image();
              pre.onload = function(){
                  iLoaded.call(this);
                  pre.onload = function(){}; //wipe out this onload function
                };
              pre.src = this.Source[1];
            }
          });
      })
      .end(); //revert the filter to maintain chaining
  }; //end jQuery.fn.jqDock()

  /***************************************************************************************************
  *  jQuery.jqDock()
  *  ===============
  * usage:    $.jqDock(property);
  * returns:  the jqDock object's property, or null
  * example:  var vsn = $.jqDock('version');
  ***************************************************************************************************/
  $.jqDock = function(x){
    return jqDock[x] ? jqDock[x] : null;
  }; //end jQuery.jqDock()
} //end of if()
})(jQuery);

