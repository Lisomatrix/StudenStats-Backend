webpackJsonp([26],{1348:function(e,i,n){"use strict";function t(e,i){if(!(e instanceof i))throw new TypeError("Cannot call a class as a function")}function a(e,i){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!i||"object"!==typeof i&&"function"!==typeof i?e:i}function o(e,i){if("function"!==typeof i&&null!==i)throw new TypeError("Super expression must either be null or a function, not "+typeof i);e.prototype=Object.create(i&&i.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),i&&(Object.setPrototypeOf?Object.setPrototypeOf(e,i):e.__proto__=i)}Object.defineProperty(i,"__esModule",{value:!0});var r=n(172),l=(n.n(r),n(173)),s=n.n(l),c=n(579),d=(n.n(c),n(580)),p=n.n(d),m=n(570),u=(n.n(m),n(574)),A=n.n(u),f=n(575),h=(n.n(f),n(576)),b=n.n(h),C=n(169),v=(n.n(C),n(170)),g=n.n(v),w=n(168),x=(n.n(w),n(90)),E=n.n(x),B=n(0),y=n.n(B),D=n(88),N=n(1349),k=(n.n(N),n(578)),_=function(){function e(e,i){for(var n=0;n<i.length;n++){var t=i[n];t.enumerable=t.enumerable||!1,t.configurable=!0,"value"in t&&(t.writable=!0),Object.defineProperty(e,t.key,t)}}return function(i,n,t){return n&&e(i.prototype,n),t&&e(i,t),i}}(),O=function(e){function i(){var e,n,o,r;t(this,i);for(var l=arguments.length,s=Array(l),c=0;c<l;c++)s[c]=arguments[c];return n=o=a(this,(e=i.__proto__||Object.getPrototypeOf(i)).call.apply(e,[this].concat(s))),o.state={dataChanged:!0,editMode:!1,data:[],modalVisible:!1,addDisciplineName:"",addAbbreviationName:"",editAbbreviationName:"",editDisciplineName:"",editDisciplineId:void 0},o.columns=[{title:"Abrevia\xe7\xe3o",dataIndex:"abbreviation",width:"10%",key:"abbreviation",render:function(e,i,n){return y.a.createElement("a",{href:"javascript:;"},e)}},{title:"Disciplina",dataIndex:"discipline",width:"70%",key:"discipline"},{title:"Editar",dataIndex:"action",width:"10%",key:"action",render:function(e,i,n){return y.a.createElement(E.a,{onClick:function(){return o._showEditModal(i.key,i.discipline,i.abbreviation)},icon:"edit",type:"dashed",shape:"circle"})}}],o._onModalOk=function(){o.state.isEdit?o.props.requestUpdateDiscipline({name:o.state.editDisciplineName,abbreviation:o.state.editAbbreviationName},o.state.editDisciplineId).then(function(e){e?o.setState({modalVisible:!1,dataChanged:!0},function(){g.a.success("Disciplina alterado!")}):g.a.error("Ocurreu um erro ao alterar a disciplina!")}):o.props.requestNewDiscipline({name:o.state.editDisciplineName,abbreviation:o.state.editAbbreviationName}).then(function(e){e?o.setState({modalVisible:!1,dataChanged:!0},function(){g.a.success("Disciplina adicionada!")}):g.a.error("Ocurreu um erro ao adicionar a disciplina!")})},o._showEditModal=function(e,i,n){o.setState({modalVisible:!0,isEdit:!0,editDisciplineId:e,editDisciplineName:i,editAbbreviationName:n})},o._showCreateModal=function(){o.setState({isEdit:!1,modalVisible:!0})},r=n,a(o,r)}return o(i,e),_(i,[{key:"componentWillMount",value:function(){var e=this;this.props.disciplines||this.props.requestDisciplines().then(function(){return e.setState({dataChanged:!0})})}},{key:"componentDidMount",value:function(){this.forceUpdate()}},{key:"componentWillUpdate",value:function(e,i){if(i.dataChanged&&e.disciplines){for(var n=[],t=0;t<e.disciplines.length;t++)n.push({key:e.disciplines[t].disciplineId,discipline:e.disciplines[t].name,abbreviation:e.disciplines[t].abbreviation});i.data=n,i.dataChanged=!1}}},{key:"render",value:function(){var e=this;return y.a.createElement("div",{className:"discipline-management-container"},y.a.createElement("div",{className:"ant-card-bordered animated slideInUp discipline-management flex-column"},y.a.createElement("div",{className:"flex-row"},y.a.createElement("div",{className:"white-space-container"}),y.a.createElement("div",{className:"add-discipline-container"},y.a.createElement(E.a,{onClick:this._showCreateModal,type:"primary"},"Adicionar Disciplina"))),y.a.createElement(b.a,{style:{marginTop:"18px"},dataSource:this.state.data,columns:this.columns}),y.a.createElement(s.a,{onOk:this._onModalOk,onCancel:function(){return e.setState({modalVisible:!1})},okText:this.state.isEdit?"Guardar Altera\xe7\xf5es":"Adicionar Disciplina",visible:this.state.modalVisible},this.state.isEdit?y.a.createElement("div",null,y.a.createElement("h2",null,"Editar Disciplina"),y.a.createElement("div",{className:"course-modal-container flex-column"},y.a.createElement(p.a.Item,{label:"Nome"},y.a.createElement(A.a,{onChange:function(i){return e.setState({editDisciplineName:i.target.value})},value:this.state.editDisciplineName,placeholder:"Nome da disciplina..."})),y.a.createElement(p.a.Item,{label:"Abrevia\xe7\xe3o"},y.a.createElement(A.a,{onChange:function(i){return e.setState({editAbbreviationName:i.target.value})},value:this.state.editAbbreviationName,placeholder:"Abrevia\xe7\xe3o da disciplina..."})))):y.a.createElement("div",null,y.a.createElement("h2",null,"Adicionar Disciplina"),y.a.createElement("div",{className:"course-modal-container flex-column"},y.a.createElement(p.a.Item,{label:"Nome"},y.a.createElement(A.a,{onChange:function(i){return e.setState({addAbbreviationName:i.target.value})},value:this.state.addAbbreviationName,placeholder:"Nome da disciplina..."})),y.a.createElement(p.a.Item,{label:"Abrevia\xe7\xe3o"},y.a.createElement(A.a,{onChange:function(i){return e.setState({editDisciplineName:i.target.value})},value:this.state.editDisciplineName,placeholder:"Abrevia\xe7\xe3o da disciplina..."})))))))}}]),i}(y.a.Component),S={requestDisciplines:k.b,requestUpdateDiscipline:k.g,requestNewDiscipline:k.c},I=function(e){return{disciplines:e.disciplines.disciplines}};i.default=Object(D.b)(I,S)(O)},1349:function(e,i,n){var t=n(1350);"string"===typeof t&&(t=[[e.i,t,""]]);var a={hmr:!1};a.transform=void 0;n(565)(t,a);t.locals&&(e.exports=t.locals)},1350:function(e,i,n){i=e.exports=n(564)(!0),i.push([e.i,"@media (max-width:600px){.discipline-management-container{padding:0!important;height:100%;padding-top:8px!important}.white-space-container{width:0!important}.add-discipline-container{width:100%!important}.add-discipline-container button{width:100%!important;height:40px}}.discipline-management-container{padding-top:24px;padding-right:24px;background-color:transparent!important}.discipline-management{-webkit-animation-duration:.3s;animation-duration:.3s;padding:18px;background-color:#fff}.white-space-container{width:100%}.add-discipline-container{width:auto}.flex-row{-ms-flex-flow:row;flex-flow:row}.flex-column,.flex-row{display:-ms-flexbox;display:flex}.flex-column{-ms-flex-flow:column;flex-flow:column}","",{version:3,sources:["C:/Users/Lisomatrix/Desktop/NEW_PAP/bac/Frontend/src/styles/disciplines-management.less"],names:[],mappings:"AAAA,yBACE,iCACE,oBAAwB,AACxB,YAAa,AACb,yBAA4B,CAC7B,AACD,uBACE,iBAAoB,CACrB,AACD,0BACE,oBAAuB,CACxB,AACD,iCACE,qBAAuB,AACvB,WAAa,CACd,CACF,AACD,iCACE,iBAAkB,AAClB,mBAAoB,AACpB,sCAAyC,CAC1C,AACD,uBACE,+BAAiC,AACzB,uBAAyB,AACjC,aAAc,AACd,qBAA0B,CAC3B,AACD,uBACE,UAAY,CACb,AACD,0BACE,UAAY,CACb,AACD,UAGE,kBAAmB,AACf,aAAe,CACpB,AACD,uBALE,oBAAqB,AACrB,YAAc,CASf,AALD,aAGE,qBAAsB,AAClB,gBAAkB,CACvB",file:"disciplines-management.less",sourcesContent:["@media (max-width: 600px) {\n  .discipline-management-container {\n    padding: 0px !important;\n    height: 100%;\n    padding-top: 8px !important;\n  }\n  .white-space-container {\n    width: 0 !important;\n  }\n  .add-discipline-container {\n    width: 100% !important;\n  }\n  .add-discipline-container button {\n    width: 100% !important;\n    height: 40px;\n  }\n}\n.discipline-management-container {\n  padding-top: 24px;\n  padding-right: 24px;\n  background-color: transparent !important;\n}\n.discipline-management {\n  -webkit-animation-duration: 0.3s;\n          animation-duration: 0.3s;\n  padding: 18px;\n  background-color: #ffffff;\n}\n.white-space-container {\n  width: 100%;\n}\n.add-discipline-container {\n  width: auto;\n}\n.flex-row {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: row;\n      flex-flow: row;\n}\n.flex-column {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: column;\n      flex-flow: column;\n}\n"],sourceRoot:""}])}});
//# sourceMappingURL=26.51076f9a.chunk.js.map