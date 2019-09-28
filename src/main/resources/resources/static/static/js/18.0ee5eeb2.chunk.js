webpackJsonp([18],{1329:function(e,t,n){"use strict";function a(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function r(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!==typeof t&&"function"!==typeof t?e:t}function o(e,t){if("function"!==typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}Object.defineProperty(t,"__esModule",{value:!0});var s=n(570),i=(n.n(s),n(574)),l=n.n(i),c=n(575),u=(n.n(c),n(576)),d=n.n(u),m=n(168),p=(n.n(m),n(90)),f=n.n(p),h=n(172),A=(n.n(h),n(173)),C=n.n(A),g=n(169),y=(n.n(g),n(170)),b=n.n(y),w=n(617),x=(n.n(w),n(607)),v=n.n(x),B=n(606),E=(n.n(B),n(600)),k=n.n(E),T=n(92),D=(n.n(T),n(11)),_=n.n(D),j=n(573),N=(n.n(j),n(572)),S=n.n(N),q=n(0),O=n.n(q),R=n(88),I=n(1330),M=(n.n(I),n(595)),Y=n(624),L=n(797),P=n(616),z=(n.n(P),n(798)),W=n.n(z),U=function(){function e(e,t){for(var n=0;n<t.length;n++){var a=t[n];a.enumerable=a.enumerable||!1,a.configurable=!0,"value"in a&&(a.writable=!0),Object.defineProperty(e,a.key,a)}}return function(t,n,a){return n&&e(t.prototype,n),a&&e(t,a),t}}(),V=S.a.Option,F=[1,2,3],G=[],K=function(e){function t(){var e,n,o,s;a(this,t);for(var i=arguments.length,l=Array(i),c=0;c<i;c++)l[c]=arguments[c];return n=o=r(this,(e=t.__proto__||Object.getPrototypeOf(t)).call.apply(e,[this].concat(l))),o.state={selectedYear:-1,selectedCourse:-1,data:[],filtersChanged:!0,newClassModalVisible:!1,newClassYear:1,newClassCourse:null,newClassDirector:null,addClassLoading:!1,newClassName:""},o.columns=[{title:"Ano",dataIndex:"year",key:"year"},{title:"Curso",dataIndex:"course",key:"course"},{title:"N Alunos",dataIndex:"studentNumber",key:"studentNumber"},{title:"Diretor de Turma",dataIndex:"classDirector",key:"classDirector"},{dataIndex:"actions",key:"actions",render:function(e,t,n){var a=O.a.createElement(k.a,{style:{width:"140px"}},O.a.createElement(k.a.Item,{onClick:function(){return o.props.history.push("/class/"+t.key)},className:"context-menu-item small-menu-item",key:"1"}," ",O.a.createElement(_.a,{style:{fontSize:"20px"},type:"edit"})," Editar"),O.a.createElement(k.a.Item,{onClick:function(){return o._removeClass(t.key)},className:"context-menu-item small-menu-item",key:"3"}," ",O.a.createElement(_.a,{style:{fontSize:"20px"},type:"delete"})," Eliminar"));return O.a.createElement(v.a,{overlay:a,trigger:["click"]},O.a.createElement("a",{href:"javascript:;",className:"ant-dropdown-link"},"Op\xe7\xf5es ",O.a.createElement(_.a,{type:"down"})))}}],o.mobileColumns=[{title:"Diretor de Turma",dataIndex:"classDirector",key:"classDirector",width:"65%"},{dataIndex:"actions",key:"actions",render:function(e,t,n){var a=O.a.createElement(k.a,{style:{width:"140px"}},O.a.createElement(k.a.Item,{onClick:function(){return o.props.history.push("/class/"+t.key)},className:"context-menu-item small-menu-item",key:"1"}," ",O.a.createElement(_.a,{style:{fontSize:"20px"},type:"edit"})," Editar"),O.a.createElement(k.a.Item,{onClick:function(){return o._removeClass(t.key)},className:"context-menu-item small-menu-item",key:"3"}," ",O.a.createElement(_.a,{style:{fontSize:"20px"},type:"delete"})," Eliminar"));return O.a.createElement(v.a,{overlay:a,trigger:["click"]},O.a.createElement("a",{href:"javascript:;",className:"ant-dropdown-link"},"Op\xe7\xf5es ",O.a.createElement(_.a,{type:"down"})))}}],o._generateRows=function(e,t,n,a){var r,o=[];if(a)for(var s=0;s<a.length;s++)if(a[s].courseId===t){r=a[s].name;break}for(var s=0;s<e.length;s++)(e[s].year===n&&e[s].course.trim().toLowerCase()===r||-1===n&&-1===t||-1===n&&e[s].course===r||e[s].year===n&&-1===t)&&o.push({key:e[s].classId,year:e[s].year,course:e[s].course,studentNumber:e[s].students.length,classDirector:e[s].classDirectorName});return o},o._getCourseIdByName=function(e){if(o.props.courses)for(var t=0;t<o.props.courses.length;t++)if(o.props.courses[t].name.trim().toLowerCase()===e.trim().toLowerCase())return o.props.courses[t].courseId;return""},o._addClass=function(){if(!o.state.newClassCourse||!o.state.newClassDirector||!o.state.newClassYear)return void(o.state.newClassCourse?o.state.newClassDirector?o.error.newClassYear||b.a.error("Selecione um ano!"):b.a.error("Selecione um diretor de turma!"):b.a.error("Selecione um curso!"));o.setState({addClassLoading:!0},function(){o.props.requestNewClass({name:o.state.newClassName,courseId:o.state.selectedCourse,year:o.state.newClassYear,teacherId:o.state.newClassDirector}).then(function(e){e?(b.a.success("Turma adicionada!"),o.setState({filtersChanged:!0,addClassLoading:!1,newClassModalVisible:!1})):(b.a.error("Ocurreu um erro ao adicionar a turma"),o.setState({newClassModalVisible:!1}))})})},o._removeClass=function(e){C.a.confirm({title:"Eliminar Turma",content:"Tem a certeza que pretende eliminar esta turma?",okText:"Sim",okType:"danger",cancelText:"Cancelar",onOk:function(){o.props.requestRemoveClass(e).then(function(e){e?(b.a.success("Turma eliminada com sucesso!"),o.setState({filtersChanged:!0})):b.a.error("Ocorreu um erro ao tentar elimar a turma!")})}})},s=n,r(o,s)}return o(t,e),U(t,[{key:"componentWillMount",value:function(){var e=this;0===G.length&&(G=F.map(function(e){return O.a.createElement(V,{key:e,value:e},e)})),this.props.classes||this.props.requestAdminClasses().then(function(){return e.setState({filtersChanged:!0})}),this.props.courses||this.props.requestCourses().then(function(){return e.setState({filtersChanged:!0})}),this.props.students||this.props.requestAdminStudents(),this.props.teachers||this.props.requestAdminTeachers(),this.props.freeTeachers||this.props.requestFreeTeachers()}},{key:"componentDidMount",value:function(){this.forceUpdate()}},{key:"componentWillUpdate",value:function(e,t){(t.filtersChanged&&e.classes||e.classes&&0===t.data.length)&&(t.data=this._generateRows(e.classes,t.selectedCourse,t.selectedYear,e.courses),t.filtersChanged=!1)}},{key:"render",value:function(){var e,t,n=this;return this.props.courses&&(e=this.props.courses.map(function(e){return O.a.createElement(V,{key:e.courseId,value:e.courseId},e.name)})),this.props.freeTeachers&&(t=this.props.freeTeachers.map(function(e){return O.a.createElement(V,{key:e.teacherId,value:e.teacherId},e.name)})),O.a.createElement("div",{className:"classes-management-container"},O.a.createElement("div",{className:"ant-card-bordered classes-management animated slideInUp flex-column"},O.a.createElement("div",{className:"classes-management-select-container flex-row"},O.a.createElement("div",{className:"flex-column select"},O.a.createElement("label",null,"Ano:"),O.a.createElement(S.a,{value:this.state.selectedYear,onChange:function(e){return n.setState({selectedYear:e,filtersChanged:!0})},placeholder:"Ano..."},G,O.a.createElement(V,{value:-1},"Todos"))),O.a.createElement("div",{className:"flex-column select"},O.a.createElement("label",null,"Curso:"),O.a.createElement(S.a,{value:this.state.selectedCourse,onChange:function(e){return n.setState({selectedCourse:Object(P.isString)(e)?e.trim().toLowerCase():e,filtersChanged:!0})},placeholder:"Curso..."},e,O.a.createElement(V,{value:-1},"Todos"))),O.a.createElement("div",{style:{width:"100%"}}),O.a.createElement(f.a,{onClick:function(){return n.setState({newClassModalVisible:!0})},className:"add-class-btn",type:"primary"},"Adicionar Turma")),O.a.createElement("div",{className:"classes-management-table-container"},O.a.createElement(d.a,{style:{marginLeft:window.innerWidth>600?"5px":"0",marginRight:window.innerWidth>600?"5px":"0"},columns:window.innerWidth>600?this.columns:this.mobileColumns,dataSource:this.state.data}))),O.a.createElement(C.a,{visible:this.state.newClassModalVisible,onCancel:function(){return n.setState({newClassModalVisible:!1})},onOk:this._addClass,okText:"Adicionar Turma",okButtonProps:{loading:this.state.addClassLoading}},O.a.createElement("div",{className:"new-class-modal-container flex-column"},O.a.createElement("div",{className:"new-class-modal-selects-container flex-row"},O.a.createElement("div",{style:{width:"30%"},className:"flex-column select"},O.a.createElement("label",null,"Ano:"),O.a.createElement(S.a,{onChange:function(e){return n.setState({newClassYear:e})},placeholder:"Ano..."},G)),O.a.createElement("div",{style:{width:"70%"},className:"flex-column select"},O.a.createElement("label",null,"Diretor de Turma:"),O.a.createElement(S.a,{onChange:function(e){return n.setState({newClassDirector:e})},placeholder:"Diretor de Turma..."},t))),O.a.createElement("div",{className:"new-class-modal-selects-container flex-row"},O.a.createElement("div",{style:{width:"70%",marginTop:"18px"},className:"flex-column select"},O.a.createElement("label",null,"Curso:"),O.a.createElement(S.a,{onChange:function(e){return n.setState({newClassCourse:e.trim().toLowerCase()})},placeholder:"Curso..."},e)),O.a.createElement("div",{style:{width:"70%",marginTop:"18px"},className:"flex-column select"},O.a.createElement("label",null,"Nome da Turma:"),O.a.createElement(l.a,{value:this.state.newClassName,onChange:function(e){return n.setState({newClassName:e.target.value})},placeholder:"Nome da Turma..."}))))))}}]),t}(O.a.Component),Q=function(e){return{classes:e.classes.classes,courses:e.classes.courses,students:e.students.students,teachers:e.teachers.teachers,freeTeachers:e.teachers.freeTeachers}},H={requestAdminClasses:M.c,requestCourses:M.e,requestAdminStudents:Y.a,requestAdminTeachers:L.a,requestNewClass:M.g,requestDirectorTeachers:L.b,requestFreeTeachers:L.c,requestRemoveClass:M.i};t.default=W()(Object(R.b)(Q,H)(K))},1330:function(e,t,n){var a=n(1331);"string"===typeof a&&(a=[[e.i,a,""]]);var r={hmr:!1};r.transform=void 0;n(565)(a,r);a.locals&&(e.exports=a.locals)},1331:function(e,t,n){t=e.exports=n(564)(!0),t.push([e.i,"body{background-color:rgba(0,0,0,.5)}@media (max-width:600px){.cal{margin:0!important}}.ant-card-bordered{background-color:#f8f8f8!important}.cal-container{padding-left:0;padding-top:0;overflow-y:scroll;height:-webkit-fill-available}.cal{margin:24px;margin-left:0;padding:8px;background-color:#f8f8f8;min-height:-webkit-fill-available;margin-bottom:100px;-webkit-animation-duration:.3s!important;animation-duration:.3s!important}.ant-menu-dark{background-color:#001529!important}.ant-menu-light{background-color:#fff!important}.anticon-bell,.anticon-down,.anticon-menu-fold,.anticon-menu-unfold,.user-label-container .name-container .name{color:rgba(0,0,0,.65)!important}.primary-text,.rc-color-picker-panel-params-input{color:rgba(0,0,0,.65)}@media (max-width:600px){.classes-management-container{padding:8px!important}.classes-management-select-container{-ms-flex-flow:column!important;flex-flow:column!important}.select{margin:0!important}.add-class-btn{margin-top:18px!important;width:100%!important;height:40px!important}}.classes-management-container{padding-right:24px;padding-top:24px}.classes-management{padding:8px;-webkit-animation-duration:.3s;animation-duration:.3s}.classes-management-table-container{margin-top:18px}.flex-row{-ms-flex-flow:row;flex-flow:row}.flex-column,.flex-row{display:-ms-flexbox;display:flex}.flex-column{-ms-flex-flow:column;flex-flow:column}.select{min-width:120px;margin-left:8px}.add-class-btn{margin-top:30px;margin-right:8px}.small-menu-item{width:140px!important;max-width:140px!important;min-width:140px!important}","",{version:3,sources:["C:/Users/Lisomatrix/Desktop/NEW_PAP/bac/Frontend/src/styles/classes-management.less"],names:[],mappings:"AAIA,KACE,+BAAqC,CACtC,AACD,yBACE,KACE,kBAAuB,CACxB,CACF,AACD,mBACE,kCAAqC,CACtC,AACD,eACE,eAAgB,AAChB,cAAe,AACf,kBAAmB,AACnB,6BAA+B,CAChC,AACD,KACE,YAAa,AACb,cAAe,AACf,YAAa,AACb,yBAA0B,AAC1B,kCAAmC,AACnC,oBAAqB,AACrB,yCAA4C,AACpC,gCAAoC,CAC7C,AACD,eACE,kCAAqC,CACtC,AACD,gBACE,+BAAqC,CACtC,AACD,gHAKE,+BAAsC,CACvC,AAID,kDACE,qBAA2B,CAC5B,AACD,yBACE,8BACE,qBAAwB,CACzB,AACD,qCACE,+BAAiC,AAC7B,0BAA6B,CAClC,AACD,QACE,kBAAqB,CACtB,AACD,eACE,0BAA4B,AAC5B,qBAAuB,AACvB,qBAAwB,CACzB,CACF,AACD,8BACE,mBAAoB,AACpB,gBAAkB,CACnB,AACD,oBACE,YAAa,AACb,+BAAiC,AACzB,sBAAyB,CAClC,AACD,oCACE,eAAiB,CAClB,AACD,UAGE,kBAAmB,AACf,aAAe,CACpB,AACD,uBALE,oBAAqB,AACrB,YAAc,CASf,AALD,aAGE,qBAAsB,AAClB,gBAAkB,CACvB,AACD,QACE,gBAAiB,AACjB,eAAiB,CAClB,AACD,eACE,gBAAiB,AACjB,gBAAkB,CACnB,AACD,iBACE,sBAAwB,AACxB,0BAA4B,AAC5B,yBAA4B,CAC7B",file:"classes-management.less",sourcesContent:["/* stylelint-disable at-rule-empty-line-before,at-rule-name-space-after,at-rule-no-unknown */\n/* stylelint-disable no-duplicate-selectors */\n/* stylelint-disable */\n/* stylelint-disable declaration-bang-space-before,no-duplicate-selectors,string-no-newline */\nbody {\n  background-color: rgba(0, 0, 0, 0.5);\n}\n@media (max-width: 600px) {\n  .cal {\n    margin: 0px !important;\n  }\n}\n.ant-card-bordered {\n  background-color: #f8f8f8 !important;\n}\n.cal-container {\n  padding-left: 0;\n  padding-top: 0;\n  overflow-y: scroll;\n  height: -webkit-fill-available;\n}\n.cal {\n  margin: 24px;\n  margin-left: 0;\n  padding: 8px;\n  background-color: #f8f8f8;\n  min-height: -webkit-fill-available;\n  margin-bottom: 100px;\n  -webkit-animation-duration: 0.3s !important;\n          animation-duration: 0.3s !important;\n}\n.ant-menu-dark {\n  background-color: #001529 !important;\n}\n.ant-menu-light {\n  background-color: #ffffff !important;\n}\n.anticon-menu-fold,\n.anticon-menu-unfold,\n.anticon-bell,\n.user-label-container .name-container .name,\n.anticon-down {\n  color: rgba(0, 0, 0, 0.65) !important;\n}\n.primary-text {\n  color: rgba(0, 0, 0, 0.65);\n}\n.rc-color-picker-panel-params-input {\n  color: rgba(0, 0, 0, 0.65);\n}\n@media (max-width: 600px) {\n  .classes-management-container {\n    padding: 8px !important;\n  }\n  .classes-management-select-container {\n    -ms-flex-flow: column !important;\n        flex-flow: column !important;\n  }\n  .select {\n    margin: 0 !important;\n  }\n  .add-class-btn {\n    margin-top: 18px !important;\n    width: 100% !important;\n    height: 40px !important;\n  }\n}\n.classes-management-container {\n  padding-right: 24px;\n  padding-top: 24px;\n}\n.classes-management {\n  padding: 8px;\n  -webkit-animation-duration: 0.3s;\n          animation-duration: 0.3s;\n}\n.classes-management-table-container {\n  margin-top: 18px;\n}\n.flex-row {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: row;\n      flex-flow: row;\n}\n.flex-column {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: column;\n      flex-flow: column;\n}\n.select {\n  min-width: 120px;\n  margin-left: 8px;\n}\n.add-class-btn {\n  margin-top: 30px;\n  margin-right: 8px;\n}\n.small-menu-item {\n  width: 140px !important;\n  max-width: 140px !important;\n  min-width: 140px !important;\n}\n"],sourceRoot:""}])},797:function(e,t,n){"use strict";function a(){return function(e){var t=localStorage.getItem("token");if(t)return fetch(c.a.httpServerURL+"/admin/teacher",{method:"GET",headers:{Accept:"application/json","Content-Type":"application/json",Authorization:t}}).then(function(e){return e.json()}).then(function(t){t&&!t.status&&e(l(t))})}}function r(){return function(e){var t=localStorage.getItem("token");if(t)return fetch(c.a.httpServerURL+"/admin/teacher/free",{method:"GET",headers:{Accept:"application/json","Content-Type":"application/json",Authorization:t}}).then(function(e){return e.json()}).then(function(t){t&&!t.status&&e(i(t))})}}function o(){return function(e){var t=localStorage.getItem("token");if(t)return fetch(c.a.httpServerURL+"/admin/teacher/director",{method:"GET",headers:{Accept:"application/json","Content-Type":"application/json",Authorization:t}}).then(function(e){return e.json()}).then(function(t){t&&!t.status&&e(s(t))})}}function s(e){return{type:u.x,payload:e}}function i(e){return{type:u.z,payload:e}}function l(e){return{type:u.S,payload:e}}t.a=a,t.c=r,t.b=o;var c=n(91),u=n(5)},798:function(e,t,n){"use strict";t.__esModule=!0;var a=n(799),r=function(e){return e&&e.__esModule?e:{default:e}}(a);t.default=r.default},799:function(e,t,n){"use strict";function a(e){return e&&e.__esModule?e:{default:e}}function r(e,t){var n={};for(var a in e)t.indexOf(a)>=0||Object.prototype.hasOwnProperty.call(e,a)&&(n[a]=e[a]);return n}t.__esModule=!0;var o=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var a in n)Object.prototype.hasOwnProperty.call(n,a)&&(e[a]=n[a])}return e},s=n(0),i=a(s),l=n(1),c=a(l),u=n(181),d=a(u),m=n(800),p=a(m),f=function(e){var t=function(t){var n=t.wrappedComponentRef,a=r(t,["wrappedComponentRef"]);return i.default.createElement(p.default,{children:function(t){return i.default.createElement(e,o({},a,t,{ref:n}))}})};return t.displayName="withRouter("+(e.displayName||e.name)+")",t.WrappedComponent=e,t.propTypes={wrappedComponentRef:c.default.func},(0,d.default)(t,e)};t.default=f},800:function(e,t,n){"use strict";function a(e){return e&&e.__esModule?e:{default:e}}function r(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function o(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!==typeof t&&"function"!==typeof t?e:t}function s(e,t){if("function"!==typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}t.__esModule=!0;var i=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var a in n)Object.prototype.hasOwnProperty.call(n,a)&&(e[a]=n[a])}return e},l=n(13),c=a(l),u=n(12),d=a(u),m=n(0),p=a(m),f=n(1),h=a(f),A=n(801),C=a(A),g=function(e){return 0===p.default.Children.count(e)},y=function(e){function t(){var n,a,s;r(this,t);for(var i=arguments.length,l=Array(i),c=0;c<i;c++)l[c]=arguments[c];return n=a=o(this,e.call.apply(e,[this].concat(l))),a.state={match:a.computeMatch(a.props,a.context.router)},s=n,o(a,s)}return s(t,e),t.prototype.getChildContext=function(){return{router:i({},this.context.router,{route:{location:this.props.location||this.context.router.route.location,match:this.state.match}})}},t.prototype.computeMatch=function(e,t){var n=e.computedMatch,a=e.location,r=e.path,o=e.strict,s=e.exact,i=e.sensitive;if(n)return n;(0,d.default)(t,"You should not use <Route> or withRouter() outside a <Router>");var l=t.route,c=(a||l.location).pathname;return(0,C.default)(c,{path:r,strict:o,exact:s,sensitive:i},l.match)},t.prototype.componentWillMount=function(){(0,c.default)(!(this.props.component&&this.props.render),"You should not use <Route component> and <Route render> in the same route; <Route render> will be ignored"),(0,c.default)(!(this.props.component&&this.props.children&&!g(this.props.children)),"You should not use <Route component> and <Route children> in the same route; <Route children> will be ignored"),(0,c.default)(!(this.props.render&&this.props.children&&!g(this.props.children)),"You should not use <Route render> and <Route children> in the same route; <Route children> will be ignored")},t.prototype.componentWillReceiveProps=function(e,t){(0,c.default)(!(e.location&&!this.props.location),'<Route> elements should not change from uncontrolled to controlled (or vice versa). You initially used no "location" prop and then provided one on a subsequent render.'),(0,c.default)(!(!e.location&&this.props.location),'<Route> elements should not change from controlled to uncontrolled (or vice versa). You provided a "location" prop initially but omitted it on a subsequent render.'),this.setState({match:this.computeMatch(e,t.router)})},t.prototype.render=function(){var e=this.state.match,t=this.props,n=t.children,a=t.component,r=t.render,o=this.context.router,s=o.history,i=o.route,l=o.staticContext,c=this.props.location||i.location,u={match:e,location:c,history:s,staticContext:l};return a?e?p.default.createElement(a,u):null:r?e?r(u):null:"function"===typeof n?n(u):n&&!g(n)?p.default.Children.only(n):null},t}(p.default.Component);y.propTypes={computedMatch:h.default.object,path:h.default.string,exact:h.default.bool,strict:h.default.bool,sensitive:h.default.bool,component:h.default.func,render:h.default.func,children:h.default.oneOfType([h.default.func,h.default.node]),location:h.default.object},y.contextTypes={router:h.default.shape({history:h.default.object.isRequired,route:h.default.object.isRequired,staticContext:h.default.object})},y.childContextTypes={router:h.default.object.isRequired},t.default=y},801:function(e,t,n){"use strict";t.__esModule=!0;var a=n(96),r=function(e){return e&&e.__esModule?e:{default:e}}(a),o={},s=0,i=function(e,t){var n=""+t.end+t.strict+t.sensitive,a=o[n]||(o[n]={});if(a[e])return a[e];var i=[],l=(0,r.default)(e,i,t),c={re:l,keys:i};return s<1e4&&(a[e]=c,s++),c},l=function(e){var t=arguments.length>1&&void 0!==arguments[1]?arguments[1]:{},n=arguments[2];"string"===typeof t&&(t={path:t});var a=t,r=a.path,o=a.exact,s=void 0!==o&&o,l=a.strict,c=void 0!==l&&l,u=a.sensitive,d=void 0!==u&&u;if(null==r)return n;var m=i(r,{end:s,strict:c,sensitive:d}),p=m.re,f=m.keys,h=p.exec(e);if(!h)return null;var A=h[0],C=h.slice(1),g=e===A;return s&&!g?null:{path:r,url:"/"===r&&""===A?"/":A,isExact:g,params:f.reduce(function(e,t,n){return e[t.name]=C[n],e},{})}};t.default=l}});
//# sourceMappingURL=18.0ee5eeb2.chunk.js.map