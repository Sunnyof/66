System.register("chunks:///_virtual/Constant.ts",["cc"],(function(t){"use strict";var n,s;return{setters:[function(t){n=t.cclegacy,s=t._decorator}],execute:function(){var c,e;n._RF.push({},"92ad7u/uXZCAbTH/w9T9nkW","Constant",void 0);const{ccclass:a,property:o}=s;t("Constant",a("Constant")(((e=class{}).GameType={DEFAULT:2},c=e))||c);n._RF.pop()}}}));

System.register("chunks:///_virtual/GameManger.ts",["./rollupPluginModLoBabelHelpers.js","cc"],(function(e){"use strict";var t,i,s,l,n,o,a,r,h,c,d,p,u,g,m,v;return{setters:[function(e){t=e.applyDecoratedDescriptor,i=e.initializerDefineProperty},function(e){s=e.cclegacy,l=e.Prefab,n=e._decorator,o=e.Component,a=e.PhysicsSystem2D,r=e.instantiate,h=e.math,c=e.Label,d=e.Input,p=e.director,u=e.view,g=e.Collider2D,m=e.Contact2DType,v=e.native}],execute:function(){var y,b,C,N,B,f,_,S,G,P,x,E,w,D;s._RF.push({},"e81dbrpG5hBHZUejhsTqruf","GameManger",void 0);const{ccclass:T,property:z}=n;e("GameManger",(y=T("GameManger"),b=z({displayName:"关卡1",type:l}),C=z({displayName:"关卡2",type:l}),N=z({displayName:"关卡3",type:l}),B=z({displayName:"关卡4",type:l}),f=z({displayName:"关卡5",type:l}),y(((D=class e extends o{constructor(...e){super(...e),i(this,"level1",G,this),i(this,"level2",P,this),i(this,"level3",x,this),i(this,"level4",E,this),i(this,"level5",w,this),this.fish=null,this.levelpre=null,this.levelnum=0,this.CurrentNode=null,this.NextNode=null,this.button=null,this.score_lable=null,this.level_lable=null,this.score=0,this.Add_score=!0,this.again_button=null,this.move_speed=-1.5,this.move_start=!0}start(){this.node.parent.parent.getChildByName("GameEnd").active=!1,a.instance.enable=!0,this.CurrentNode=this.node.getChildByName("StartBG"),this.button=this.node.parent.getChildByName("Button"),this.score_lable=this.node.parent.getChildByName("fixation").getChildByName("score").getChildByName("score_label"),this.level_lable=this.node.parent.getChildByName("fixation").getChildByName("level").getChildByName("level_label"),this.fish=this.node.parent.getChildByName("fish"),this.again_button=this.node.parent.parent.getChildByName("GameEnd").getChildByName("Button").getChildByName("again"),this.setInputActive(!0),this.schedule(this.change,.5),this.collison()}create(e){switch(e){case 1:this.levelpre=r(this.level1);break;case 2:this.levelpre=r(this.level2);break;case 3:this.levelpre=r(this.level3);break;case 4:this.levelpre=r(this.level4);break;case 5:this.levelpre=r(this.level5)}return this.levelpre.setWorldPosition(this.levelpre.getWorldPosition().x,1450,0),this.levelpre.parent=this.node,this.levelpre}change(){this.CurrentNode.getWorldPosition().y<1e3&&(this.NextNode=this.create(h.randomRangeInt(1,6)),this.CurrentNode=this.NextNode,this.levelnum++,this.level_lable.getComponent(c).string=String(this.levelnum)),this.Add_score&&this.score++,this.score_lable.getComponent(c).string=String(this.score)}update(e){this.fish&&this.move_start&&(150==this.fish.getPosition().x&&(this.move_speed=-1.5),-150==this.fish.getPosition().x&&(this.move_speed=1.5),this.fish.setPosition(this.fish.getPosition().x+this.move_speed,this.fish.getPosition().y,0))}setInputActive(e){this.button.on(d.EventType.TOUCH_MOVE,this.touch,this),this.again_button.on(d.EventType.TOUCH_END,(()=>{p.loadScene("GameHome")}),this)}touch(e){let t=u.getVisibleSize(),i=e.getUILocation();this.button.setPosition(i.x-t.width/2,i.y-t.height/2+60)}collison(){this.fish.getComponent(g)&&this.fish.getComponent(g).on(m.BEGIN_CONTACT,this.onBeginContact,this)}onBeginContact(){console.log("游戏结束！"),this.score>e.Best_score&&(e.Best_score=this.score),this.fish&&(this.move_start=!1),this.fish.getComponent(g).enabled=!1,this.Add_score=!1,a.instance.enable=!1,this.node.parent.parent.getChildByName("GameEnd").active=!0,this.node.parent.parent.getChildByName("GameEnd").getChildByName("Score").getChildByName("ScoreLabel").getComponent(c).string=String(this.score),this.node.parent.parent.getChildByName("GameEnd").getChildByName("BestScore").getChildByName("BestScoreLabel").getComponent(c).string=String(e.Best_score),v.reflection.callStaticMethod("com/cocos/game/SDKLog","showAd","(Ljava/lang/String;)V","")}}).Best_score=0,G=t((S=D).prototype,"level1",[b],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return null}}),P=t(S.prototype,"level2",[C],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return null}}),x=t(S.prototype,"level3",[N],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return null}}),E=t(S.prototype,"level4",[B],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return null}}),w=t(S.prototype,"level5",[f],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return null}}),_=S))||_));s._RF.pop()}}}));

System.register("chunks:///_virtual/GameStart.ts",["cc"],(function(t){"use strict";var e,r,a,c;return{setters:[function(t){e=t.cclegacy,r=t.Component,a=t.director,c=t._decorator}],execute:function(){var s;e._RF.push({},"3a250YdVrpE46drIKIA2MIP","GameStart",void 0);const{ccclass:o,property:n}=c;t("GameStart",o("GameStart")(s=class extends r{start(){}touch(){a.loadScene("Loading")}update(t){}})||s);e._RF.pop()}}}));

System.register("chunks:///_virtual/loading.ts",["cc"],(function(e){"use strict";var i,t,s,o;return{setters:[function(e){i=e.cclegacy,t=e.Component,s=e.director,o=e._decorator}],execute:function(){var n;i._RF.push({},"0b1968JHXpIIpWBHFKno52m","loading",void 0);const{ccclass:a,property:d}=o;e("loading",a("loading")(n=class extends t{constructor(...e){super(...e),this.fishA1=void 0,this.fishA2=void 0,this.fishB1=void 0,this.fishB2=void 0}start(){this.schedule(this.loading,.1)}loading(){for(var e=0;e<1e3;e++)setTimeout((()=>{this.node.getChildByName("fishA1").angle+=1,this.node.getChildByName("fishA2").angle+=1,this.node.getChildByName("fishB1").angle+=1,this.node.getChildByName("fishB2").angle+=1}),4e-5*e);s.loadScene("GameHome")}update(e){}})||n);i._RF.pop()}}}));

System.register("chunks:///_virtual/main",["./Constant.ts","./GameManger.ts","./GameStart.ts","./loading.ts"],(function(){"use strict";return{setters:[null,null,null,null],execute:function(){}}}));

(function(r) {
  r('virtual:///prerequisite-imports/main', 'chunks:///_virtual/main'); 
})(function(mid, cid) {
    System.register(mid, [cid], function (_export, _context) {
    return {
        setters: [function(_m) {
            var _exportObj = {};

            for (var _key in _m) {
              if (_key !== "default" && _key !== "__esModule") _exportObj[_key] = _m[_key];
            }
      
            _export(_exportObj);
        }],
        execute: function () { }
    };
    });
});