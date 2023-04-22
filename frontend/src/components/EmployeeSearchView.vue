<template>

    <v-data-table
        :headers="headers"
        :items="employeeSearch"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'EmployeeSearchView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            employeeSearch : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/employeeSearches'))

            temp.data._embedded.employeeSearches.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.employeeSearch = temp.data._embedded.employeeSearches;
        },
        methods: {
        }
    }
</script>

