webpackJsonp([27],{1351:function(e,t,n){"use strict";function i(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function a(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!==typeof t&&"function"!==typeof t?e:t}function l(e,t){if("function"!==typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}function o(e,t){for(var n=[],i=0;i<e.length;i++)e[i].disciplineId===t&&n.push(e[i]);return n}function r(e,t){for(var n=0;n<e.length;n++)if(e[n].disciplineId===t)return e[n].name;return"NOT FOUND"}function s(e,t){for(var n=0,i=t.length;n<i;n++)if(t[n].moduleId===e)return t[n];return{name:"NOT FOUND!",hours:0,disciplineId:1}}Object.defineProperty(t,"__esModule",{value:!0});var d=n(172),c=(n.n(d),n(173)),u=n.n(c),m=n(579),p=(n.n(m),n(580)),f=n.n(p),A=n(570),h=(n.n(A),n(574)),C=n.n(h),x=n(575),w=(n.n(x),n(576)),g=n.n(w),E=n(169),B=(n.n(E),n(170)),v=n.n(B),b=n(168),y=(n.n(b),n(90)),D=n.n(y),N=n(573),I=(n.n(N),n(572)),k=n.n(I),M=n(0),O=n.n(M),U=n(88),S=n(1352),_=(n.n(S),n(578)),q=n(589),F=function(){function e(e,t){for(var n=0;n<t.length;n++){var i=t[n];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(e,i.key,i)}}return function(t,n,i){return n&&e(t.prototype,n),i&&e(t,i),t}}(),H=k.a.Option,j=function(e){function t(){var e,n,l,o;i(this,t);for(var r=arguments.length,d=Array(r),c=0;c<r;c++)d[c]=arguments[c];return n=l=a(this,(e=t.__proto__||Object.getPrototypeOf(t)).call.apply(e,[this].concat(d))),l.state={dataChanged:!0,selectedDiscipline:-1,data:[],editModuleId:void 0,editName:"",editDisciplineId:void 0,editHours:0,addName:"",addDisciplineId:void 0,addHours:0},l.columns=[{title:"Nome",dataIndex:"name",width:"30%",key:"name",render:function(e,t,n){return O.a.createElement("a",{href:"javascript:;"},e)}},{title:"Disciplina",dataIndex:"discipline",width:"50%",key:"discipline"},{title:"N Horas",dataIndex:"hours",width:"10%",key:"hours"},{title:"Editar",dataIndex:"action",width:"10%",key:"action",render:function(e,t,n){return O.a.createElement(D.a,{onClick:function(){return l._showEditModal(t.key,t.discipline,t.abbreviation)},icon:"edit",type:"dashed",shape:"circle"})}}],l.mobileColumns=[{title:"Nome",dataIndex:"name",width:"30%",key:"name",render:function(e,t,n){return O.a.createElement("a",{href:"javascript:;"},e)}},{title:"Disciplina",dataIndex:"discipline",width:"50%",key:"discipline"},{title:"Editar",dataIndex:"action",width:"10%",key:"action",render:function(e,t,n){return O.a.createElement(D.a,{onClick:function(){return l._showEditModal(t.key,t.discipline,t.abbreviation)},icon:"edit",type:"dashed",shape:"circle"})}}],l._showEditModal=function(e){var t=s(e,l.props.modules);l.setState({modalVisible:!0,isEdit:!0,editDisciplineId:t.disciplineId,editName:t.name,editHours:t.hours,editModuleId:e})},l._showCreateModal=function(){l.setState({isEdit:!1,modalVisible:!0})},l._onModalOk=function(){l.state.isEdit?l.props.requestUpdateModule({name:l.state.editName,disciplineId:l.state.editDisciplineId,hours:l.state.editHours},l.state.editModuleId).then(function(e){e?l.setState({modalVisible:!1,dataChanged:!0},function(){v.a.success("M\xf3dulo/UFCD alterado!")}):v.a.error("Ocurreu um erro ao alterar o M\xf3dulo/UFCD!")}):l.props.requestNewModule({name:l.state.addName,disciplineId:l.state.addDisciplineId,hours:l.state.addHours}).then(function(e){e?l.setState({modalVisible:!1,dataChanged:!0},function(){v.a.success("M\xf3dulo/UFCD adicionada!")}):v.a.error("Ocurreu um erro ao adicionar o M\xf3dulo/UFCD!")})},o=n,a(l,o)}return l(t,e),F(t,[{key:"componentWillMount",value:function(){var e=this;this.props.disciplines||this.props.requestDisciplines().then(function(t){return e.setState({dataChanged:!0})}),this.props.modules||this.props.requestModules().then(function(t){return e.setState({dataChanged:!0})})}},{key:"componentDidMount",value:function(){this.forceUpdate()}},{key:"componentWillUpdate",value:function(e,t){var n=t.selectedDiscipline,i=t.dataChanged,a=e.modules,l=e.disciplines;if(i&&a&&l){var s,d=[],c=[];-1===n?c=a:(s=r(l,n),c=o(a,n));for(var u=0,m=c.length;u<m;u++)d.push({key:c[u].moduleId,name:c[u].name,discipline:-1===n?r(l,c[u].disciplineId):s,hours:c[u].hours});t.data=d,t.dataChanged=!1}}},{key:"render",value:function(){var e=this,t=this.props.disciplines,n=[];return t&&(n=t.map(function(e){return O.a.createElement(H,{key:e.disciplineId,value:e.disciplineId},e.abbreviation)})),O.a.createElement("div",{className:"discipline-modules-management-container"},O.a.createElement("div",{className:"ant-card-bordered discipline-modules-management animated slideInUp"},O.a.createElement("div",{className:"filters-container flex-row"},O.a.createElement("div",{className:"discipline-filters-container flex-row"},O.a.createElement("div",{className:"discipline-filter-select-container flex-column"},O.a.createElement("label",null,"Disciplina"),O.a.createElement(k.a,{onChange:function(t){return e.setState({selectedDiscipline:t,dataChanged:!0})},value:this.state.selectedDiscipline,style:{minWidth:"120px"},placeholder:"Disciplina..."},n,O.a.createElement(H,{value:-1},"Todas")))),O.a.createElement("div",{className:"white-space-container"}),O.a.createElement("div",{className:"add-discipline-module-container"},O.a.createElement(D.a,{className:"add-btn",onClick:this._showCreateModal,type:"primary"},"Adicionar M\xf3dulo/UFCD"))),O.a.createElement(g.a,{style:{marginTop:"18px"},dataSource:this.state.data,columns:window.innerWidth>600?this.columns:this.mobileColumns})),O.a.createElement(u.a,{onOk:this._onModalOk,onCancel:function(){return e.setState({modalVisible:!1})},okText:this.state.isEdit?"Guardar Altera\xe7\xf5es":"Adicionar Curso",visible:this.state.modalVisible},this.state.isEdit?O.a.createElement("div",null,O.a.createElement("h2",null,"Editar M\xf3dulo/UFCD"),O.a.createElement("div",{className:"module-modal-container flex-column"},O.a.createElement(f.a.Item,{label:"Nome"},O.a.createElement(C.a,{onChange:function(t){return e.setState({editName:t.target.value})},value:this.state.editName,placeholder:"Nome do M\xf3dulo/UFCD..."})),O.a.createElement(f.a.Item,{label:"Discipline"},O.a.createElement(k.a,{className:"discipline-filter-select-container",style:{width:"100%"},onChange:function(t){return e.setState({editDisciplineId:t})},value:this.state.editDisciplineId,placeholder:"Disciplina..."},n)),O.a.createElement(f.a.Item,{label:"N Horas"},O.a.createElement(C.a,{onChange:function(t){return e.setState({editHours:t.target.value})},value:this.state.editHours,placeholder:"N horas..."})))):O.a.createElement("div",null,O.a.createElement("h2",null,"Adicionar M\xf3dulo/UFCD"),O.a.createElement("div",{className:"course-modal-container flex-column"},O.a.createElement(f.a.Item,{label:"Nome"},O.a.createElement(C.a,{onChange:function(t){return e.setState({addName:t.target.value})},value:this.state.addName,placeholder:"Nome do M\xf3dulo/UFCD..."})),O.a.createElement(k.a,{className:"discipline-filter-select-container",style:{width:"100%"},onChange:function(t){return e.setState({addDisciplineId:t})},value:this.state.addDisciplineId,placeholder:"Disciplina..."},n),O.a.createElement(f.a.Item,{label:"N Horas"},O.a.createElement(C.a,{onChange:function(t){return e.setState({addHours:t.target.value})},value:this.state.addHours,placeholder:"N horas..."}))))))}}]),t}(O.a.Component),P={requestDisciplines:_.b,requestModules:q.a,requestNewModule:q.d,requestUpdateModule:q.e},T=function(e){return{disciplines:e.disciplines.disciplines,modules:e.modules.modules}};t.default=Object(U.b)(T,P)(j)},1352:function(e,t,n){var i=n(1353);"string"===typeof i&&(i=[[e.i,i,""]]);var a={hmr:!1};a.transform=void 0;n(565)(i,a);i.locals&&(e.exports=i.locals)},1353:function(e,t,n){t=e.exports=n(564)(!0),t.push([e.i,"@media (max-width:600px){.discipline-modules-management-container{padding:8px!important;max-height:calc(100% - 60px)!important;overflow:scroll}.discipline-modules-management{padding:8px!important}.filters-container{-ms-flex-flow:column!important;flex-flow:column!important}.add-btn,.discipline-filter-select-container{width:100%!important}.add-btn{height:40px!important}}.discipline-modules-management-container{padding-top:24px;padding-right:24px;background-color:transparent!important}.discipline-modules-management{-webkit-animation-duration:.3s;animation-duration:.3s;padding:18px;background-color:#fff}.white-space-container{width:100%}.add-discipline-module-container{width:auto;padding-top:20px}.discipline-filters-container{display:-ms-flexbox;display:flex;-ms-flex-flow:column;flex-flow:column}.discipline-filter-select-container{min-width:120px}.flex-row{-ms-flex-flow:row;flex-flow:row}.flex-column,.flex-row{display:-ms-flexbox;display:flex}.flex-column{-ms-flex-flow:column;flex-flow:column}","",{version:3,sources:["C:/Users/Lisomatrix/Desktop/NEW_PAP/bac/Frontend/src/styles/discipline-modules-management.less"],names:[],mappings:"AAAA,yBACE,yCACE,sBAAwB,AACxB,uCAAyC,AACzC,eAAiB,CAClB,AACD,+BACE,qBAAwB,CACzB,AACD,mBACE,+BAAiC,AAC7B,0BAA6B,CAClC,AAID,6CAFE,oBAAuB,CAKxB,AAHD,SAEE,qBAAwB,CACzB,CACF,AACD,yCACE,iBAAkB,AAClB,mBAAoB,AACpB,sCAAyC,CAC1C,AACD,+BACE,+BAAiC,AACzB,uBAAyB,AACjC,aAAc,AACd,qBAA0B,CAC3B,AACD,uBACE,UAAY,CACb,AACD,iCACE,WAAY,AACZ,gBAAkB,CACnB,AACD,8BACE,oBAAqB,AACrB,aAAc,AACd,qBAAsB,AAClB,gBAAkB,CACvB,AACD,oCACE,eAAiB,CAClB,AACD,UAGE,kBAAmB,AACf,aAAe,CACpB,AACD,uBALE,oBAAqB,AACrB,YAAc,CASf,AALD,aAGE,qBAAsB,AAClB,gBAAkB,CACvB",file:"discipline-modules-management.less",sourcesContent:["@media (max-width: 600px) {\n  .discipline-modules-management-container {\n    padding: 8px !important;\n    max-height: calc(100% - 60px) !important;\n    overflow: scroll;\n  }\n  .discipline-modules-management {\n    padding: 8px !important;\n  }\n  .filters-container {\n    -ms-flex-flow: column !important;\n        flex-flow: column !important;\n  }\n  .discipline-filter-select-container {\n    width: 100% !important;\n  }\n  .add-btn {\n    width: 100% !important;\n    height: 40px !important;\n  }\n}\n.discipline-modules-management-container {\n  padding-top: 24px;\n  padding-right: 24px;\n  background-color: transparent !important;\n}\n.discipline-modules-management {\n  -webkit-animation-duration: 0.3s;\n          animation-duration: 0.3s;\n  padding: 18px;\n  background-color: #ffffff;\n}\n.white-space-container {\n  width: 100%;\n}\n.add-discipline-module-container {\n  width: auto;\n  padding-top: 20px;\n}\n.discipline-filters-container {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: column;\n      flex-flow: column;\n}\n.discipline-filter-select-container {\n  min-width: 120px;\n}\n.flex-row {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: row;\n      flex-flow: row;\n}\n.flex-column {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: column;\n      flex-flow: column;\n}\n"],sourceRoot:""}])}});
//# sourceMappingURL=27.4628efd0.chunk.js.map