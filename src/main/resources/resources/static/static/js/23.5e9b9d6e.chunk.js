webpackJsonp([23],{1315:function(e,t,n){"use strict";function s(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function a(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!==typeof t&&"function"!==typeof t?e:t}function l(e,t){if("function"!==typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}function d(e,t){for(var n=[],s=0;s<e.length;s++)e[s].moduleId===t&&n.push(e[s]);return n}Object.defineProperty(t,"__esModule",{value:!0});var r=n(575),i=(n.n(r),n(576)),o=n.n(i),u=n(573),c=(n.n(u),n(572)),m=n.n(c),p=n(0),A=n.n(p),f=n(88),x=n(795),C=(n.n(x),n(589)),g=n(601),B=n(578),w=n(596),h=function(){function e(e,t){for(var n=0;n<t.length;n++){var s=t[n];s.enumerable=s.enumerable||!1,s.configurable=!0,"value"in s&&(s.writable=!0),Object.defineProperty(e,s.key,s)}}return function(t,n,s){return n&&e(t.prototype,n),s&&e(t,s),t}}(),y=m.a.Option,b=0,v=function(e){var t=function(e){var t=[{title:"N de Teste",dataIndex:"number",key:"number",width:"10%"},{title:"Data",dataIndex:"date",key:"date",width:"80%"},{title:"Nota",dataIndex:"grade",key:"grade",width:"10%"}];return A.a.createElement(o.a,{columns:t,dataSource:e,pagination:!1})},n=[{title:"Disciplina",dataIndex:"discipline",key:"discipline",render:function(e,t,n){return A.a.createElement("span",null,e)}},{title:"M\xf3dulo/UFCD",dataIndex:"module",key:"module",render:function(e,t,n){return A.a.createElement("span",null,e)}},{title:"N Testes",dataIndex:"testsNumber",key:"testsNumber",render:function(e,t,n){return A.a.createElement("span",null,e)}},{title:"M\xe9dia de Testes",dataindex:"media",key:"media",render:function(e,t,n){return A.a.createElement("span",null,0!==e.media?"0":e.media)}},{title:"Nota de m\xf3dulo/UFCD",dataindex:"moduleGrade",key:"moduleGrade",render:function(e,t,n){return A.a.createElement("span",null,e.moduleGrade)}}];return A.a.createElement(o.a,{columns:n,expandedRowRender:function(n){var s=d(e.gradeRows,n.key);return t(s)},dataSource:e.rows})},E=function(e){return A.a.createElement("div",{style:{animationDelay:"0."+e.delay+"s"},className:"module-grade-card-container animated slideInLeft"},A.a.createElement("div",{className:"flex-row"},A.a.createElement("div",{style:{width:"100%",padding:"2px",paddingBottom:"4px"}},A.a.createElement("span",null,A.a.createElement("b",null,"Disciplina:")),A.a.createElement("br",null),A.a.createElement("span",null,e.discipline))),A.a.createElement("div",{className:"flex-row"},A.a.createElement("div",{style:{width:"100%",padding:"2px",paddingBottom:"4px"}},A.a.createElement("span",null,A.a.createElement("b",null,"M\xf3dulo/UFCD:")," ",e.module))),A.a.createElement("div",{className:"flex-row"},A.a.createElement("div",{style:{width:"50%",padding:"2px",paddingBottom:"4px"}},A.a.createElement("span",null,A.a.createElement("b",null,"N Testes:")," ",e.testsNumber)),A.a.createElement("div",{style:{width:"50%",padding:"2px",paddingBottom:"4px"}},A.a.createElement("span",null,A.a.createElement("b",null,"M\xe9dia de Testes:")," ",e.media))),A.a.createElement("div",{style:{textAlign:"center",padding:"2px",paddingBottom:"4px"}},A.a.createElement("span",null,A.a.createElement("b",null,"Nota de m\xf3dulo/UFCD:")," ",e.moduleGrade)))},D=function(e){function t(){var e,n,l,d;s(this,t);for(var r=arguments.length,i=Array(r),o=0;o<r;o++)i[o]=arguments[o];return n=l=a(this,(e=t.__proto__||Object.getPrototypeOf(t)).call.apply(e,[this].concat(i))),l.state={selectedDiscipline:-1,selectedModule:-1,data:[],gradeRows:[],requestedClassDisciplines:!1,requestedClassModules:!1,requestedClassTests:!1},l._getSelectedDiscipline=function(e,t){if(t)for(var n=0;n<t.length;n++)if(t[n].disciplineId===e)return t[n];return"NOT FOUND"},l._getModule=function(e,t){if(t)for(var n=0;n<t.length;n++)if(t[n].moduleId===e.moduleId)return t[n];return"NOT FOUND"},l._getTests=function(e,t){var n=[];if(t)for(var s=0;s<t.length;s++)t[s].moduleId===e&&n.push(t[s]);return n},l._getTestGrade=function(e,t){if(t)for(var n=0;n<t.length;n++)if(t[n].testId===e)return t[n]},l._getModuleGrade=function(e,t){if(t)for(var n=0;n<t.length;n++)if(t[n].moduleId===e)return t[n].moduleGrade;return 0},l._requestNeededData=function(e,t){e.disciplines||!e.studentClass||t.requestedClassDisciplines||(e.requestClassDisciplines(e.studentClass.classId),t.requestedClassDisciplines=!0),e.modules||!e.studentClass||t.requestedClassModules||(e.requestModulesByClass(e.studentClass.classId),t.requestedClassModules=!0),e.tests||!e.studentClass||t.requestedClassTests||(e.requestClassTests(e.studentClass.classId),t.requestedClassTests=!0)},d=n,a(l,d)}return l(t,e),h(t,[{key:"componentWillMount",value:function(){this.props.requestStudentModuleGrades(),this.props.requestStudentTestGrades()}},{key:"componentWillUpdate",value:function(e,t){var n=this;this._requestNeededData(e,t);var s=[],a=[];if(e.moduleGrades&&e.testGrades&&e.modules&&e.tests)for(var l=1,d=0;d<e.moduleGrades.length;d++){var r;(function(){var i=n._getModule(e.moduleGrades[d],e.modules);if(i.moduleId!==t.selectedModule&&-1!==t.selectedModule)return"continue";var o=n._getSelectedDiscipline(i.disciplineId,e.disciplines);if(o.disciplineId!==t.selectedDiscipline&&-1!==t.selectedDiscipline)return"continue";var u=n._getTests(i.moduleId,e.tests),c=[];u.forEach(function(t){var s=n._getTestGrade(t.testId,e.testGrades);s||(s={testGradeId:l,number:l,date:t.date,grade:0,moduleId:i.moduleId}),c.push(s),a.push({key:s.testGradeId,number:l,date:t.date,grade:s.grade?s.grade:0,moduleId:i.moduleId}),l++}),r=0,c.forEach(function(e){r+=e.grade});var m=n._getModuleGrade(i.moduleId,e.moduleGrades);s.push({key:i.moduleId,discipline:o.name,module:i.name,testsNumber:u.length,media:u.length>0?(r/u.length).toFixed(2):0,moduleGrade:m})})()}t.data=s,t.gradeRows=a}},{key:"render",value:function(){var e,t,n=this;return b=0,this.props.disciplines&&(e=this.props.disciplines.map(function(e){return A.a.createElement(y,{key:e.disciplineId,value:e.disciplineId},e.abbreviation)})),this.props.modules&&(t=this.props.modules.map(function(e){if(e.disciplineId===n.state.selectedDiscipline)return A.a.createElement(y,{key:e.moduleId,value:e.moduleId},e.name)})),A.a.createElement("div",{className:"students-tests-container"},A.a.createElement("div",{className:"ant-card-bordered students-tests animated slideInUp"},A.a.createElement("div",{className:"students-tests-options"},A.a.createElement("div",{className:"select select-margin"},A.a.createElement("label",null,"Disciplina:"),A.a.createElement(m.a,{value:this.state.selectedDiscipline,onChange:function(e){return n.setState({selectedDiscipline:e,selectedModule:-1})},placeholder:"Disciplina..."},e,A.a.createElement(y,{value:-1},"Todos"))),A.a.createElement("div",{className:"select select-margin"},A.a.createElement("label",null,"M\xf3dulo/UFCD:"),A.a.createElement(m.a,{value:this.state.selectedModule,onChange:function(e){return n.setState({selectedModule:e})},placeholder:"Modules..."},t,A.a.createElement(y,{value:-1},"Todos")))),window.innerWidth>600?A.a.createElement("div",{className:"test-cotent-container"},A.a.createElement(v,{rows:this.state.data,gradeRows:this.state.gradeRows})):""),window.innerWidth<600?this.state.data.map(function(e){return b++,A.a.createElement(E,{delay:b,key:e.key,discipline:e.discipline,media:e.media,module:e.module,moduleGrade:e.moduleGrade,testsNumber:e.testsNumber})}):"")}}]),t}(A.a.Component),I=function(e){return{tests:e.tests.tests,studentClass:e.classes.studentClass,modules:e.modules.modules,moduleGrades:e.modules.moduleGrades,testGrades:e.grades.testGrades,disciplines:e.disciplines.classDisciplines}},k={requestStudentModuleGrades:g.e,requestStudentTestGrades:g.f,requestClassDisciplines:B.a,requestModulesByClass:C.b,requestClassTests:w.b};t.default=Object(f.b)(I,k)(D)},795:function(e,t,n){var s=n(796);"string"===typeof s&&(s=[[e.i,s,""]]);var a={hmr:!1};a.transform=void 0;n(565)(s,a);s.locals&&(e.exports=s.locals)},796:function(e,t,n){t=e.exports=n(564)(!0),t.push([e.i,"@media (max-width:600px){.students-tests-container{padding:0!important;max-height:calc(100% - 60px);overflow:auto}.students-tests{max-height:100%;overflow:auto}}.students-tests-container{padding-right:24px;padding-top:24px}.students-tests{background-color:#fff;border-radius:.5em;padding:8px;-webkit-animation-duration:.3s;animation-duration:.3s}.students-tests .students-tests-options{display:-ms-flexbox;display:flex;-ms-flex-flow:row;flex-flow:row}.select{display:-ms-flexbox;display:flex;-ms-flex-flow:column;flex-flow:column;min-width:120px}.select-margin{margin-left:8px}.switch{display:-ms-flexbox;display:flex;-ms-flex-flow:row;flex-flow:row;padding-top:24px}.module-grade-card-container{border-radius:.5em;padding:8px;margin:8px;background-color:#fff}.flex-row{-ms-flex-flow:row;flex-flow:row}.flex-column,.flex-row{display:-ms-flexbox;display:flex}.flex-column{-ms-flex-flow:column;flex-flow:column}","",{version:3,sources:["C:/Users/Lisomatrix/Desktop/NEW_PAP/bac/Frontend/src/styles/students-tests.less"],names:[],mappings:"AAAA,yBACE,0BACE,oBAAsB,AACtB,6BAA8B,AAC9B,aAAe,CAChB,AACD,gBACE,gBAAiB,AACjB,aAAe,CAChB,CACF,AACD,0BACE,mBAAoB,AACpB,gBAAkB,CACnB,AACD,gBACE,sBAAuB,AACvB,mBAAqB,AACrB,YAAa,AACb,+BAAiC,AACzB,sBAAyB,CAClC,AACD,wCACE,oBAAqB,AACrB,aAAc,AACd,kBAAmB,AACf,aAAe,CACpB,AACD,QACE,oBAAqB,AACrB,aAAc,AACd,qBAAsB,AAClB,iBAAkB,AACtB,eAAiB,CAClB,AACD,eACE,eAAiB,CAClB,AACD,QACE,oBAAqB,AACrB,aAAc,AACd,kBAAmB,AACf,cAAe,AACnB,gBAAkB,CACnB,AACD,6BACE,mBAAqB,AACrB,YAAa,AACb,WAAY,AACZ,qBAAuB,CACxB,AACD,UAGE,kBAAmB,AACf,aAAe,CACpB,AACD,uBALE,oBAAqB,AACrB,YAAc,CASf,AALD,aAGE,qBAAsB,AAClB,gBAAkB,CACvB",file:"students-tests.less",sourcesContent:["@media (max-width: 600px) {\n  .students-tests-container {\n    padding: 0 !important;\n    max-height: calc(100% - 60px);\n    overflow: auto;\n  }\n  .students-tests {\n    max-height: 100%;\n    overflow: auto;\n  }\n}\n.students-tests-container {\n  padding-right: 24px;\n  padding-top: 24px;\n}\n.students-tests {\n  background-color: #fff;\n  border-radius: 0.5em;\n  padding: 8px;\n  -webkit-animation-duration: 0.3s;\n          animation-duration: 0.3s;\n}\n.students-tests .students-tests-options {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: row;\n      flex-flow: row;\n}\n.select {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: column;\n      flex-flow: column;\n  min-width: 120px;\n}\n.select-margin {\n  margin-left: 8px;\n}\n.switch {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: row;\n      flex-flow: row;\n  padding-top: 24px;\n}\n.module-grade-card-container {\n  border-radius: 0.5em;\n  padding: 8px;\n  margin: 8px;\n  background-color: #fff;\n}\n.flex-row {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: row;\n      flex-flow: row;\n}\n.flex-column {\n  display: -ms-flexbox;\n  display: flex;\n  -ms-flex-flow: column;\n      flex-flow: column;\n}\n"],sourceRoot:""}])}});
//# sourceMappingURL=23.5e9b9d6e.chunk.js.map