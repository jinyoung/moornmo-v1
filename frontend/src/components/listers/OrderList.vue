<template>
    <div>
        <v-list two-line>
            <template>
                <v-list-item v-for="(data, n) in values" :key="n">
                    <v-list-item-avatar color="grey darken-1">
                        <v-img :src="data.photo ? data.photo:'https://cdn.vuetifyjs.com/images/cards/cooking.png'"/>
                    </v-list-item-avatar>

                    <v-list-item-content>
                        <v-list-item-title style="margin-bottom:10px;">
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                        </v-list-item-title>

                        <v-list-item-subtitle style="font-size:25px; font-weight:700;">
                            [ Id :  {{data.id }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ AccountDate :  {{data.accountDate }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ Type :  {{data.type }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ Number :  {{data.number }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ Client :  {{data.client }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ OrderDate :  {{data.orderDate }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ DeliveryDate :  {{data.deliveryDate }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ Manager :  {{data.manager }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ RegisterName :  {{data.registerName }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ OrderItem :  {{data.orderItem }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ OrderCount :  {{data.orderCount }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ Cost :  {{data.cost }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ Memo :  {{data.memo }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        </v-list-item-subtitle>

                    </v-list-item-content>
                </v-list-item>

                <v-divider v-if="n !== 6" :key="`divider-${n}`" inset></v-divider>
            </template>
        </v-list>

        <v-col style="margin-bottom:40px;">
            <div class="text-center">
                <v-dialog
                        v-model="openDialog"
                        width="332.5"
                        fullscreen
                        hide-overlay
                        transition="dialog-bottom-transition"
                >
                    <template v-slot:activator="{ on, attrs }">
                        <v-fab-transition>
                            <v-btn
                                    color="blue"
                                    fab
                                    dark
                                    large
                                    style="position:absolute; bottom: 5%; right: 2%; z-index:99"
                                    @click="openDialog=true;"
                            >
                                <v-icon v-bind="attrs" v-on="on">mdi-plus</v-icon>
                            </v-btn>
                        </v-fab-transition>
                    </template>

                    <Order :offline="offline" class="video-card" :isNew="true" :editMode="true" v-model="newValue" @add="append" v-if="tick"/>
                
                    <v-btn
                            style="postition:absolute; top:2%; right:2%"
                            @click="closeDialog()"
                            depressed 
                            icon 
                            absolute
                    >
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-dialog>
            </div>
        </v-col>
    </div>
</template>

<script>
    const axios = require('axios').default;
    import Order from './../Order.vue';

    export default {
        name: 'OrderManager',
        components: {
            Order,
        },
        props: {
            offline: Boolean,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            values: [],
            newValue: {},
            tick : true,
            openDialog : false,
        }),
        async created() {
            if(this.offline){
                if(!this.values) this.values = [];
                return;
            } 

            var temp = await axios.get(axios.fixUrl('/orders'))
            temp.data._embedded.orders.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])
            this.values = temp.data._embedded.orders;
            
            this.newValue = {
                'accountDate': '2023-04-22',
                'type': '',
                'number': 0,
                'client': '',
                'orderDate': '2023-04-22',
                'deliveryDate': '2023-04-22',
                'manager': '',
                'registerName': '',
                'orderItem': 0,
                'orderCount': 0,
                'cost': 0,
                'memo': '',
            }
        },
        methods: {
            closeDialog(){
                this.openDialog = false
            },
            append(value){
                this.tick = false
                this.newValue = {}
                this.values.push(value)
                
                this.$emit('input', this.values);

                this.$nextTick(function(){
                    this.tick=true
                })
            }
        },
    };
</script>


<style>
    .video-card {
        width:300px; 
        margin-left:4.5%; 
        margin-top:50px; 
        margin-bottom:50px;
    }
</style>

