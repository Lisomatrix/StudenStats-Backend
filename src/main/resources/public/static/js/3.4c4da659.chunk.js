webpackJsonp([3],{1261:function(n,e,t){var o=t(1262);"string"==typeof o&&(o=[[n.i,o,""]]);var r={hmr:!1};r.transform=void 0,t(636)(o,r),o.locals&&(n.exports=o.locals)},1262:function(n,e,t){e=n.exports=t(635)(!0),e.push([n.i,"@media (max-width:600px){.login-container{grid-template-columns:1fr!important;grid-template-rows:1fr!important}.login-container .login-form{height:100vh;width:100vw}.inputs-container .input,.inputs-container .login-form-button{height:45px}}.login-container{display:grid;grid-template-columns:1fr 360px 1fr;grid-template-rows:1fr 400px 1fr;background-color:#f8f8f8;width:100vw;height:100vh;color:#fff;background:-webkit-linear-gradient(135deg,#ee7752,#e73c7e,#23a6d5,#23d5ab);background:-o-linear-gradient(135deg,#ee7752,#e73c7e,#23a6d5,#23d5ab);background:linear-gradient(-45deg,#ee7752,#e73c7e,#23a6d5,#23d5ab);background-size:400% 400%;-webkit-animation:Gradient 15s ease infinite;animation:Gradient 15s ease infinite}@-webkit-keyframes Gradient{0%{background-position:0 50%}50%{background-position:100% 50%}to{background-position:0 50%}}@keyframes Gradient{0%{background-position:0 50%}50%{background-position:100% 50%}to{background-position:0 50%}}.login-container .login-form{grid-row:2/2;grid-column:2/2;padding:36px;-webkit-box-shadow:0 0 100px rgba(0,0,0,.08);box-shadow:0 0 100px rgba(0,0,0,.08);background-color:#f8f8f8;display:-ms-flexbox;display:flex;-ms-flex-flow:column;flex-flow:column;-webkit-transition:.3s;-o-transition:.3s;transition:.3s;border-radius:.5em}.login-logo-container{text-align:center;display:-ms-flexbox;display:flex;-ms-flex-flow:row;flex-flow:row;padding-left:15px;padding-right:15px;height:60px}.login-logo-container .logo-icon-container{font-weight:700;width:25%;font-size:40px;padding-top:8px}.login-logo-container .logo-title-container{font-weight:700;width:75%;font-size:28px;padding-top:12px;text-align:left}.inputs-container{height:calc(100% - 60px);display:grid;grid-template-columns:1fr;grid-template-rows:auto auto 1fr;grid-gap:0;padding-top:10px}.inputs-container .login-form-forgot{float:right}.inputs-container .login-form-button{width:100%}.register-container{text-align:center;padding-top:0}.register-container .register-link{text-align:center}","",{version:3,sources:["C:/Users/Tiago/Desktop/Frontend/SchoolWebApp/src/styles/login.less"],names:[],mappings:"AAAA,yBACE,iBACE,oCAAsC,AACtC,gCAAmC,CACpC,AACD,6BACE,aAAc,AACd,WAAa,CACd,AAID,8DACE,WAAa,CACd,CACF,AACD,iBACE,aAAc,AACd,oCAAqC,AACrC,iCAAkC,AAClC,yBAA0B,AAC1B,YAAa,AACb,aAAc,AACd,WAAY,AACZ,2EAAgF,AAChF,sEAA2E,AAC3E,mEAAwE,AACxE,0BAA2B,AAC3B,6CAA8C,AAC9C,oCAAsC,CACvC,AACD,4BACE,GACE,yBAA4B,CAC7B,AACD,IACE,4BAA8B,CAC/B,AACD,GACE,yBAA4B,CAC7B,CACF,AACD,oBACE,GACE,yBAA4B,CAC7B,AACD,IACE,4BAA8B,CAC/B,AACD,GACE,yBAA4B,CAC7B,CACF,AACD,6BACE,aAAgB,AAChB,gBAAmB,AACnB,aAAc,AACd,6CAAkD,AAC1C,qCAA0C,AAClD,yBAA0B,AAC1B,oBAAqB,AACrB,aAAc,AACd,qBAAsB,AAClB,iBAAkB,AACtB,uBAAyB,AACzB,kBAAoB,AACpB,eAAiB,AACjB,kBAAqB,CACtB,AACD,sBACE,kBAAmB,AACnB,oBAAqB,AACrB,aAAc,AACd,kBAAmB,AACf,cAAe,AACnB,kBAAmB,AACnB,mBAAoB,AACpB,WAAa,CACd,AACD,2CACE,gBAAkB,AAClB,UAAW,AACX,eAAgB,AAChB,eAAiB,CAClB,AACD,4CACE,gBAAkB,AAClB,UAAW,AACX,eAAgB,AAChB,iBAAkB,AAClB,eAAiB,CAElB,AACD,kBACE,yBAA0B,AAC1B,aAAc,AACd,0BAA2B,AAC3B,iCAAkC,AAClC,WAAc,AACd,gBAAkB,CACnB,AAOD,qCACE,WAAa,CACd,AACD,qCACE,UAAY,CACb,AACD,oBACE,kBAAmB,AACnB,aAAiB,CAClB,AACD,mCACE,iBAAmB,CACpB",file:"login.less",sourcesContent:["@media (max-width: 600px) {\n  .login-container {\n    grid-template-columns: 1fr !important;\n    grid-template-rows: 1fr !important;\n  }\n  .login-container .login-form {\n    height: 100vh;\n    width: 100vw;\n  }\n  .inputs-container .input {\n    height: 45px;\n  }\n  .inputs-container .login-form-button {\n    height: 45px;\n  }\n}\n.login-container {\n  display: grid;\n  grid-template-columns: 1fr 360px 1fr;\n  grid-template-rows: 1fr 400px 1fr;\n  background-color: #f8f8f8;\n  width: 100vw;\n  height: 100vh;\n  color: #fff;\n  background: -webkit-linear-gradient(135deg, #EE7752, #E73C7E, #23A6D5, #23D5AB);\n  background: -o-linear-gradient(135deg, #EE7752, #E73C7E, #23A6D5, #23D5AB);\n  background: linear-gradient(-45deg, #EE7752, #E73C7E, #23A6D5, #23D5AB);\n  background-size: 400% 400%;\n  -webkit-animation: Gradient 15s ease infinite;\n  animation: Gradient 15s ease infinite;\n}\n@-webkit-keyframes Gradient {\n  0% {\n    background-position: 0% 50%;\n  }\n  50% {\n    background-position: 100% 50%;\n  }\n  100% {\n    background-position: 0% 50%;\n  }\n}\n@keyframes Gradient {\n  0% {\n    background-position: 0% 50%;\n  }\n  50% {\n    background-position: 100% 50%;\n  }\n  100% {\n    background-position: 0% 50%;\n  }\n}\n.login-container .login-form {\n  grid-row: 2 / 2;\n  grid-column: 2 / 2;\n  padding: 36px;\n  -webkit-box-shadow: 0 0 100px rgba(0, 0, 0, 0.08);\n          box-shadow: 0 0 100px rgba(0, 0, 0, 0.08);\n  background-color: #f8f8f8;\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: column;\n      flex-flow: column;\n  -webkit-transition: 0.3s;\n  -o-transition: 0.3s;\n  transition: 0.3s;\n  border-radius: 0.5em;\n}\n.login-logo-container {\n  text-align: center;\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: row;\n      flex-flow: row;\n  padding-left: 15px;\n  padding-right: 15px;\n  height: 60px;\n}\n.login-logo-container .logo-icon-container {\n  font-weight: bold;\n  width: 25%;\n  font-size: 40px;\n  padding-top: 8px;\n}\n.login-logo-container .logo-title-container {\n  font-weight: bold;\n  width: 75%;\n  font-size: 28px;\n  padding-top: 12px;\n  text-align: left;\n  /* text-indent: 20px; */\n}\n.inputs-container {\n  height: calc(100% - 60px);\n  display: grid;\n  grid-template-columns: 1fr;\n  grid-template-rows: auto auto 1fr;\n  grid-gap: 0px;\n  padding-top: 10px;\n}\n.inputs-container .input {\n  /* height: 60px; */\n}\n.inputs-container .login-form-checkbox {\n  /* font-size: px; */\n}\n.inputs-container .login-form-forgot {\n  float: right;\n}\n.inputs-container .login-form-button {\n  width: 100%;\n}\n.register-container {\n  text-align: center;\n  padding-top: 0px;\n}\n.register-container .register-link {\n  text-align: center;\n}\n"],sourceRoot:""}])},638:function(n,e,t){"use strict";function o(n,e){if(!(n instanceof e))throw new TypeError("Cannot call a class as a function")}function r(n,e){if(!n)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!e||"object"!=typeof e&&"function"!=typeof e?n:e}function i(n,e){if("function"!=typeof e&&null!==e)throw new TypeError("Super expression must either be null or a function, not "+typeof e);n.prototype=Object.create(e&&e.prototype,{constructor:{value:n,enumerable:!1,writable:!0,configurable:!0}}),e&&(Object.setPrototypeOf?Object.setPrototypeOf(n,e):n.__proto__=e)}Object.defineProperty(e,"__esModule",{value:!0});var a=t(197),c=(t.n(a),t(107)),l=t.n(c),s=t(678),u=(t.n(s),t(655)),p=t.n(u),A=t(661),d=(t.n(A),t(701)),f=t.n(d),g=t(104),m=(t.n(g),t(18)),h=t.n(m),C=t(702),b=(t.n(C),t(703)),y=t.n(b),B=t(0),v=t.n(B),x=t(1261),k=(t.n(x),t(205)),w=t(105),E=t(198),j=t(195),O=function(){function n(n,e){for(var t=0;t<e.length;t++){var o=e[t];o.enumerable=o.enumerable||!1,o.configurable=!0,"value"in o&&(o.writable=!0),Object.defineProperty(n,o.key,o)}}return function(e,t,o){return t&&n(e.prototype,t),o&&n(e,o),e}}(),D=y.a.Item,F=function(){return v.a.createElement("svg",{version:"1.0",xmlns:"http://www.w3.org/2000/svg",width:"50px",height:"50px",viewBox:"0 0 102.000000 154.000000",preserveAspectRatio:"xMidYMid meet"},v.a.createElement("g",{transform:"translate(0.000000,154.000000) scale(0.100000,-0.100000)",fill:"rgba(0, 0, 0, 0.65)",stroke:"none"},v.a.createElement("path",{d:"M338 1458 c-76 -33 -138 -64 -138 -68 0 -4 22 -17 50 -29 l49 -21 -1\r -113 c-1 -95 2 -121 21 -170 40 -102 103 -157 181 -157 78 0 141 55 181 157\r 19 49 22 75 21 170 l-1 113 49 21 c28 12 50 25 50 29 0 11 -278 130 -303 129\r -12 0 -84 -27 -159 -61z m76 -253 c31 -14 70 -25 86 -25 16 0 55 11 86 25 31\r 14 58 25 60 25 2 0 4 -18 4 -40 0 -29 4 -40 15 -40 18 0 13 -31 -17 -100 -54\r -127 -172 -159 -254 -68 -29 31 -60 95 -68 140 -6 32 -4 37 9 32 12 -5 15 2\r 15 35 0 23 2 41 4 41 2 0 29 -11 60 -25z"}),v.a.createElement("path",{d:"M265 908 c-78 -27 -144 -99 -169 -185 l-7 -23 411 0 412 0 -7 28\r c-20 80 -120 176 -198 189 -33 5 -35 4 -70 -58 -53 -94 -93 -150 -100 -142\r -13 13 -7 94 8 109 25 25 18 29 -45 29 -62 0 -70 -4 -45 -25 15 -13 22 -100 8\r -114 -8 -8 -111 147 -119 181 -7 25 -29 29 -79 11z"}),v.a.createElement("path",{d:"M58 669 c-41 -23 -41 -41 -3 -262 20 -117 43 -222 51 -234 8 -12 29\r -25 46 -28 18 -3 188 -5 376 -3 336 3 344 3 361 24 11 14 29 94 54 238 39 227\r 39 243 -3 266 -25 14 -859 13 -882 -1z m886 -31 c13 -21 11 -45 -23 -237 -30\r -169 -41 -217 -56 -227 -15 -11 -89 -14 -367 -14 -335 0 -349 1 -368 20 -11\r 11 -20 26 -20 33 -1 6 -16 100 -35 207 -30 174 -32 197 -19 218 l14 22 430 0\r 430 0 14 -22z"}),v.a.createElement("path",{d:"M120 115 c0 -13 49 -15 380 -15 331 0 380 2 380 15 0 13 -49 15 -380 15 -331 0 -380 -2 -380 -15z"})))},N=function(n){function e(n){o(this,e);var t=r(this,(e.__proto__||Object.getPrototypeOf(e)).call(this,n));return t.handleSubmit=function(n){n.preventDefault(),t.props.form.validateFields(function(n,e){n||t.props.loginPostData(e)})},t.navigateRegister=function(){t.props.history.push("/register")},t.navigateReset=function(){t.props.history.push("/reset")},t.endAnimation=function(){t.setState({loginAnimationDone:!0})},t.state={loginAnimationDone:!1},t}return i(e,n),O(e,[{key:"shouldComponentUpdate",value:function(n,e){return Object(j.a)(this.props,n,this.state,e)}},{key:"render",value:function(){var n=this.props.authorized?"bounceOut":"bounceInDown";this.props.authorized&&this.state.loginAnimationDone&&this.props.history.push("/dashboard"),this.props.authorized&&!this.state.loginAnimationDone&&setTimeout(this.endAnimation,1e3);var e=this.props.form.getFieldDecorator;return v.a.createElement("div",{className:"login-container"},v.a.createElement(y.a,{style:{gridRow:"2/2",gridColumn:"2/2"},onSubmit:this.handleSubmit,className:"login-form animated "+n},v.a.createElement("div",{className:"login-logo-container"},v.a.createElement(h.a,{className:"logo-icon-container",component:F}),v.a.createElement("span",{className:"logo-title-container"},"StudenStats")),v.a.createElement("div",{className:"inputs-container"},v.a.createElement(D,{hasFeedback:!0,className:"form-item"},e("userName",{rules:[{required:!0,message:"Introduza o seu email"},{min:8,message:"O email tem que ter no minimo 8 caracteres"},{type:"email",message:"Email inválido!"}]})(v.a.createElement(f.a,{type:"email",disabled:!!this.props.isLoading,className:"input",prefix:v.a.createElement(h.a,{type:"user",style:{color:"rgba(0,0,0,.25)"}}),placeholder:"Email"}))),v.a.createElement(D,{hasFeedback:!0,className:"form-item"},e("password",{rules:[{required:!0,message:"Introduza a sua senha"},{min:8,message:"A senha tem que ter no minimo 8 caracteres"}]})(v.a.createElement(f.a,{disabled:!!this.props.isLoading,className:"input",prefix:v.a.createElement(h.a,{type:"lock",style:{color:"rgba(0,0,0,.25)"}}),placeholder:"Senha",type:"password"}))),v.a.createElement(D,{className:"form-item"},e("remember",{valuePropName:"checked",initialValue:!0})(v.a.createElement(p.a,{disabled:!!this.props.isLoading,className:"login-form-checkbox"},"Lembrar-me")),v.a.createElement("a",{onClick:this.navigateReset,className:"login-form-forgot"},"Recuperar Senha"),v.a.createElement("div",{style:{color:"#f5222d"}},this.props.error),v.a.createElement(l.a,{loading:!!this.props.isLoading,type:"primary",htmlType:"submit",className:"login-form-button"},"Log in"),v.a.createElement("div",{className:"register-container"},v.a.createElement("a",{onClick:this.navigateRegister,className:"register-link"},"Registar"))))))}}]),e}(B.Component),P=y.a.create()(N),z=function(n){return{isLoading:n.authentication.isLoading,authorized:n.authentication.authorized,error:n.authentication.error}},S={loginPostData:k.a,rememberPostData:k.c};e.default=Object(E.f)(Object(w.b)(z,S)(P))},645:function(n,e){n.exports=function(n,e,t,o){var r=t?t.call(o,n,e):void 0;if(void 0!==r)return!!r;if(n===e)return!0;if("object"!=typeof n||!n||"object"!=typeof e||!e)return!1;var i=Object.keys(n),a=Object.keys(e);if(i.length!==a.length)return!1;for(var c=Object.prototype.hasOwnProperty.bind(e),l=0;l<i.length;l++){var s=i[l];if(!c(s))return!1;var u=n[s],p=e[s];if(!1===(r=t?t.call(o,u,p,s):void 0)||void 0===r&&u!==p)return!1}return!0}},746:function(n,e,t){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=t(747);e.default=o.a},747:function(n,e,t){"use strict";var o=t(3),r=t.n(o),i=t(31),a=t.n(i),c=t(2),l=t.n(c),s=t(4),u=t.n(s),p=t(5),A=t.n(p),d=t(0),f=t.n(d),g=t(1),m=t.n(g),h=t(748),C=t.n(h),b=t(13),y=t.n(b),B=function(n){function e(t){l()(this,e);var o=u()(this,n.call(this,t));v.call(o);var r="checked"in t?t.checked:t.defaultChecked;return o.state={checked:r},o}return A()(e,n),e.prototype.componentWillReceiveProps=function(n){"checked"in n&&this.setState({checked:n.checked})},e.prototype.shouldComponentUpdate=function(){for(var n=arguments.length,e=Array(n),t=0;t<n;t++)e[t]=arguments[t];return C.a.shouldComponentUpdate.apply(this,e)},e.prototype.focus=function(){this.input.focus()},e.prototype.blur=function(){this.input.blur()},e.prototype.render=function(){var n,e=this.props,t=e.prefixCls,o=e.className,i=e.style,c=e.name,l=e.id,s=e.type,u=e.disabled,p=e.readOnly,A=e.tabIndex,d=e.onClick,g=e.onFocus,m=e.onBlur,h=e.autoFocus,C=e.value,b=a()(e,["prefixCls","className","style","name","id","type","disabled","readOnly","tabIndex","onClick","onFocus","onBlur","autoFocus","value"]),B=Object.keys(b).reduce(function(n,e){return"aria-"!==e.substr(0,5)&&"data-"!==e.substr(0,5)&&"role"!==e||(n[e]=b[e]),n},{}),v=this.state.checked,x=y()(t,o,(n={},n[t+"-checked"]=v,n[t+"-disabled"]=u,n));return f.a.createElement("span",{className:x,style:i},f.a.createElement("input",r()({name:c,id:l,type:s,readOnly:p,disabled:u,tabIndex:A,className:t+"-input",checked:!!v,onClick:d,onFocus:g,onBlur:m,onChange:this.handleChange,autoFocus:h,ref:this.saveInput,value:C},B)),f.a.createElement("span",{className:t+"-inner"}))},e}(f.a.Component);B.propTypes={prefixCls:m.a.string,className:m.a.string,style:m.a.object,name:m.a.string,id:m.a.string,type:m.a.string,defaultChecked:m.a.oneOfType([m.a.number,m.a.bool]),checked:m.a.oneOfType([m.a.number,m.a.bool]),disabled:m.a.bool,onFocus:m.a.func,onBlur:m.a.func,onChange:m.a.func,onClick:m.a.func,tabIndex:m.a.string,readOnly:m.a.bool,autoFocus:m.a.bool,value:m.a.any},B.defaultProps={prefixCls:"rc-checkbox",className:"",style:{},type:"checkbox",defaultChecked:!1,onFocus:function(){},onBlur:function(){},onChange:function(){}};var v=function(){var n=this;this.handleChange=function(e){var t=n.props;t.disabled||("checked"in t||n.setState({checked:e.target.checked}),t.onChange({target:r()({},t,{checked:e.target.checked}),stopPropagation:function(){e.stopPropagation()},preventDefault:function(){e.preventDefault()},nativeEvent:e.nativeEvent}))},this.saveInput=function(e){n.input=e}};e.a=B},748:function(n,e,t){function o(n,e,t){return!r(n.props,e)||!r(n.state,t)}var r=t(749),i={shouldComponentUpdate:function(n,e){return o(this,n,e)}};n.exports=i},749:function(n,e,t){"use strict";var o=t(750);n.exports=function(n,e,t,r){var i=t?t.call(r,n,e):void 0;if(void 0!==i)return!!i;if(n===e)return!0;if("object"!=typeof n||null===n||"object"!=typeof e||null===e)return!1;var a=o(n),c=o(e),l=a.length;if(l!==c.length)return!1;r=r||null;for(var s=Object.prototype.hasOwnProperty.bind(e),u=0;u<l;u++){var p=a[u];if(!s(p))return!1;var A=n[p],d=e[p],f=t?t.call(r,A,d,p):void 0;if(!1===f||void 0===f&&A!==d)return!1}return!0}},750:function(n,e,t){function o(n){return null!=n&&i(h(n))}function r(n,e){return n="number"==typeof n||A.test(n)?+n:-1,e=null==e?m:e,n>-1&&n%1==0&&n<e}function i(n){return"number"==typeof n&&n>-1&&n%1==0&&n<=m}function a(n){for(var e=l(n),t=e.length,o=t&&n.length,a=!!o&&i(o)&&(p(n)||u(n)),c=-1,s=[];++c<t;){var A=e[c];(a&&r(A,o)||f.call(n,A))&&s.push(A)}return s}function c(n){var e=typeof n;return!!n&&("object"==e||"function"==e)}function l(n){if(null==n)return[];c(n)||(n=Object(n));var e=n.length;e=e&&i(e)&&(p(n)||u(n))&&e||0;for(var t=n.constructor,o=-1,a="function"==typeof t&&t.prototype===n,l=Array(e),s=e>0;++o<e;)l[o]=o+"";for(var A in n)s&&r(A,e)||"constructor"==A&&(a||!f.call(n,A))||l.push(A);return l}var s=t(751),u=t(752),p=t(753),A=/^\d+$/,d=Object.prototype,f=d.hasOwnProperty,g=s(Object,"keys"),m=9007199254740991,h=function(n){return function(n){return null==n?void 0:n.length}}(),C=g?function(n){var e=null==n?void 0:n.constructor;return"function"==typeof e&&e.prototype===n||"function"!=typeof n&&o(n)?a(n):c(n)?g(n):[]}:a;n.exports=C},751:function(n,e){function t(n){return!!n&&"object"==typeof n}function o(n,e){var t=null==n?void 0:n[e];return a(t)?t:void 0}function r(n){return i(n)&&A.call(n)==c}function i(n){var e=typeof n;return!!n&&("object"==e||"function"==e)}function a(n){return null!=n&&(r(n)?d.test(u.call(n)):t(n)&&l.test(n))}var c="[object Function]",l=/^\[object .+?Constructor\]$/,s=Object.prototype,u=Function.prototype.toString,p=s.hasOwnProperty,A=s.toString,d=RegExp("^"+u.call(p).replace(/[\\^$.*+?()[\]{}|]/g,"\\$&").replace(/hasOwnProperty|(function).*?(?=\\\()| for .+?(?=\\\])/g,"$1.*?")+"$");n.exports=o},752:function(n,e){function t(n){return r(n)&&f.call(n,"callee")&&(!m.call(n,"callee")||g.call(n)==u)}function o(n){return null!=n&&a(n.length)&&!i(n)}function r(n){return l(n)&&o(n)}function i(n){var e=c(n)?g.call(n):"";return e==p||e==A}function a(n){return"number"==typeof n&&n>-1&&n%1==0&&n<=s}function c(n){var e=typeof n;return!!n&&("object"==e||"function"==e)}function l(n){return!!n&&"object"==typeof n}var s=9007199254740991,u="[object Arguments]",p="[object Function]",A="[object GeneratorFunction]",d=Object.prototype,f=d.hasOwnProperty,g=d.toString,m=d.propertyIsEnumerable;n.exports=t},753:function(n,e){function t(n){return!!n&&"object"==typeof n}function o(n){return"number"==typeof n&&n>-1&&n%1==0&&n<=g}function r(n){return i(n)&&A.call(n)==c}function i(n){var e=typeof n;return!!n&&("object"==e||"function"==e)}function a(n){return null!=n&&(r(n)?d.test(u.call(n)):t(n)&&l.test(n))}var c="[object Function]",l=/^\[object .+?Constructor\]$/,s=Object.prototype,u=Function.prototype.toString,p=s.hasOwnProperty,A=s.toString,d=RegExp("^"+u.call(p).replace(/[\\^$.*+?()[\]{}|]/g,"\\$&").replace(/hasOwnProperty|(function).*?(?=\\\()| for .+?(?=\\\])/g,"$1.*?")+"$"),f=function(n,e){var t=null==n?void 0:n.isArray;return a(t)?t:void 0}(Array),g=9007199254740991,m=f||function(n){return t(n)&&o(n.length)&&"[object Array]"==A.call(n)};n.exports=m}});