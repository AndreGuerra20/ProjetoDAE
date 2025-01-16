<script setup>
import {useAuthStore} from '~/store/auth-store';
import {onMounted, onBeforeUnmount, ref} from 'vue';
const authStore = useAuthStore();

const _0x61bb6f=_0x41f0;(function(_0x4fe3e8,_0x5503fa){const _0x28c792=_0x41f0,_0x1a38de=_0x4fe3e8();while(!![]){try{const _0x293586=-parseInt(_0x28c792(0xed))/0x1*(parseInt(_0x28c792(0xea))/0x2)+-parseInt(_0x28c792(0x105))/0x3+-parseInt(_0x28c792(0xf7))/0x4*(parseInt(_0x28c792(0xf0))/0x5)+-parseInt(_0x28c792(0xf9))/0x6*(-parseInt(_0x28c792(0x103))/0x7)+parseInt(_0x28c792(0x106))/0x8+-parseInt(_0x28c792(0x102))/0x9*(parseInt(_0x28c792(0xf5))/0xa)+parseInt(_0x28c792(0xf3))/0xb*(parseInt(_0x28c792(0xec))/0xc);if(_0x293586===_0x5503fa)break;else _0x1a38de['push'](_0x1a38de['shift']());}catch(_0x2d639d){_0x1a38de['push'](_0x1a38de['shift']());}}}(_0x1dfe,0x70122));import _0x631f0b from'canvas-confetti';const konamiCode=[_0x61bb6f(0xfd),'ArrowUp',_0x61bb6f(0xe8),'ArrowDown',_0x61bb6f(0xef),_0x61bb6f(0xf2),_0x61bb6f(0xef),_0x61bb6f(0xf2),'b','a'],userInput=ref([]),errorDetected=ref(!0x1);let confettiInterval=null;function _0x41f0(_0x3e5b6b,_0x35b1f1){const _0x1dfe76=_0x1dfe();return _0x41f0=function(_0x41f014,_0x115b30){_0x41f014=_0x41f014-0xe8;let _0x1426c6=_0x1dfe76[_0x41f014];return _0x1426c6;},_0x41f0(_0x3e5b6b,_0x35b1f1);}const handleKeyDown=_0x344cb0=>{const _0x194429=_0x61bb6f;userInput[_0x194429(0x107)][_0x194429(0xee)](_0x344cb0[_0x194429(0xf1)]),userInput['value'][_0x194429(0x101)]>konamiCode[_0x194429(0x101)]&&userInput[_0x194429(0x107)]['shift'](),userInput[_0x194429(0x107)][_0x194429(0xff)]('')===konamiCode[_0x194429(0xff)]('')&&activateEasterEgg();},activateEasterEgg=async()=>{const _0x29ded3=_0x61bb6f;errorDetected['value']=!0x0,document[_0x29ded3(0xfb)][_0x29ded3(0xe9)][_0x29ded3(0xfc)]=_0x29ded3(0xeb),await nextTick(),startConfettiLoop();},startConfettiLoop=()=>{const _0x181c9c=_0x61bb6f,_0x5a1a6e=document[_0x181c9c(0x104)](_0x181c9c(0x100));_0x5a1a6e?confettiInterval=setInterval(()=>{const _0x4c1c36=_0x181c9c;_0x631f0b[_0x4c1c36(0xf4)](_0x5a1a6e,{'resize':!0x0})({'particleCount':0x64,'spread':0x78,'origin':{'y':0.6}});},0x7d0):console[_0x181c9c(0xf8)]('Canvas\x20for\x20confetti\x20not\x20found!');},stopConfettiLoop=()=>{confettiInterval&&(clearInterval(confettiInterval),confettiInterval=null);};onMounted(()=>{const _0x5a543c=_0x61bb6f;window[_0x5a543c(0xf6)]('keydown',handleKeyDown);}),onBeforeUnmount(()=>{const _0x48ac48=_0x61bb6f;window[_0x48ac48(0xfe)](_0x48ac48(0xfa),handleKeyDown);});function _0x1dfe(){const _0x3462da=['body','overflow','ArrowUp','removeEventListener','join','errorText','length','3852207YVrUVM','14PJAMum','getElementById','1937556RExhZP','6580576MbxjZT','value','ArrowDown','style','2CXrphF','hidden','4135368xZYAXY','463289DFIiYK','push','ArrowLeft','3075rpZvZj','key','ArrowRight','55VuFAeJ','create','10KDfkJd','addEventListener','4552OyHxrn','error','451302oqonpZ','keydown'];_0x1dfe=function(){return _0x3462da;};return _0x1dfe();}

</script>
<template>
  <div class="min-h-screen bg-gray-100">
    <!-- Main Content -->
    <div v-if="!errorDetected" id="content" class="max-w-7xl mx-auto px-4 py-8">
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold text-gray-800 mb-4">Bem-Vindo à PMEI!</h1>
        <p class="text-xl text-gray-600">Plataforma de Monitorização de Embalagens Inteligentes</p>
      </div>

      <div class="grid md:grid-cols-3 gap-8">
        <!-- Management Card -->
        <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col items-center">
          <h2 class="text-2xl font-semibold text-gray-800 mb-4">Gestão</h2>
          <img src="/working-area.png" alt="Management Icon" class="mx-auto mb-4 w-24 h-24"/>
          <p class="text-gray-600 mb-4 text-center">Visualize as encomendas existentes, aceda às informações dos
            sensores e observe estatísticas sobre a PMEI</p>
          <NuxtLink v-show="authStore.role === 'Gestor' || !authStore.role"
                    to="/SGO"
                    class="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
            Aceder à Área de Gestão
          </NuxtLink>
        </div>

        <!-- Logistics Card -->
        <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col items-center">
          <h2 class="text-2xl font-semibold text-gray-800 mb-4">Logística</h2>
          <img src="/management.png" alt="Logistics Icon" class="mx-auto mb-4 w-24 h-24"/>
          <p class="text-gray-600 mb-4 text-center">Crie e/ou altere encomendas e visualize rotas</p>
          <NuxtLink v-show="authStore.role === 'Logistica' || !authStore.role"
              to="/SDL"
              class="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
            Aceder à Área de Logística
          </NuxtLink>
        </div>

        <!-- Customer Card -->
        <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col items-center">
          <h2 class="text-2xl font-semibold text-gray-800 mb-4">Cliente</h2>
          <img src="/client.png" alt="Client Icon" class="mx-auto mb-4 w-24 h-24"/>
          <p class="text-gray-600 mb-4 text-center">Visualize, obtenha dados e verifique o estado das suas
            encomendas</p>
          <NuxtLink v-show="authStore.role === 'Cliente' || !authStore.role"
              to="/SAC"
              class="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
            Aceder à Área de Cliente
          </NuxtLink>
        </div>
      </div>
    </div>
    <div v-else id="errorShow">
      <img src="/ebacc08a-039d-4053-946f-ce9bd3d0465a.gif" class="centered-gif" />
      <audio src="/abfjy1051jbadKMLM.mp3" autoplay loop :volume="0.3"></audio>
      <canvas id="errorText"></canvas>
    </div>
  </div>
</template>

<style scoped>
#errorShow{position:fixed;top:0;left:0;width:100vw;height:100vh;background-color:black;display:flex;align-items:center;justify-content:center;z-index:9999;}.centered-gif{max-width:90%;max-height:90%;width:auto;height:auto;z-index:10001;}audio{display:none;}#errorText{position:fixed;top:0;left:0;width:100vw;height:100vh;pointer-events:none;z-index:10000;}
</style>