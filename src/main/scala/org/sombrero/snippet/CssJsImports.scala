package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import _root_.scala.xml._

class CssJsImports {
  def render(xhtml : NodeSeq) : NodeSeq = {
    <head>
      <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
      <title>Sombrero</title>
      
      <link href="/classpath/css/layout/default-user/menu.css" rel="stylesheet" type="text/css" />
      <link href="/classpath/css/layout/default-user/my_layout.css" rel="stylesheet" type="text/css" />
      <!--[if lte IE 7]>
        <link href="/classpath/css/default/patch_my_layout.css" rel="stylesheet" type="text/css" />
      <![endif]-->
  
      <link type="text/css" href="/classpath/css/theme/sunny/jquery-ui-1.8.4.custom.css" rel="stylesheet" />
      <script type="text/javascript" src="/classpath/js/framework/jquery-1.4.2.min.js"></script>
      <script type="text/javascript" src="/classpath/js/framework/jquery-ui-1.8.4.custom.min.js"></script>

      <script type="text/javascript" src="/classpath/js/widget/ui.simplewidget.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.titlebar.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.unary.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.binary.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.analog.js"></script>

      <script type="text/javascript" src="/classpath/js/widget/ui.toolbox.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.widgetcontainer.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.navigator.js"></script>
      <link type="text/css" href="/classpath/css/widget/navigator.css" media="screen" rel="stylesheet" />
  	
      <script type="text/javascript" src="/classpath/plugin/draggable/jquery-collide.js"></script> 
      
      <script type="text/javascript" src="/classpath/plugin/fancybox/jquery.fancybox-1.3.1.pack.js"></script>
      <script type="text/javascript" src="/classpath/plugin/fancybox/jquery.easing-1.3.pack.js"></script>
      <script type="text/javascript" src="/classpath/plugin/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
      <link type="text/css" href="/classpath/plugin/fancybox/jquery.fancybox-1.3.1.css" rel="stylesheet" />
	
      <script type="text/javascript" src="/classpath/plugin/wresize/jquery.wresize.js"></script>
      { if (User.loggedIn_? && User.superUser_?){
          <script type="text/javascript" src="/classpath/plugin/sidebar/jquery.sidebar.js"></script>
          <link type="text/css" href="/classpath/plugin/sidebar/dark-glass/sidebar.css" rel="stylesheet" />
          <script type="text/javascript" src="/classpath/plugin/jquery-message/jquery.message.js"></script>
          <link type="text/css" href="/classpath/plugin/jquery-message/jquery.message.css" rel="stylesheet" />
          <script type="text/javascript" src="/classpath/plugin/uniform/jquery.uniform.js"></script>
          <link type="text/css" href="/classpath/plugin/uniform/css/uniform.default.css" rel="stylesheet" />
        }
      }
    </head>
	}
}