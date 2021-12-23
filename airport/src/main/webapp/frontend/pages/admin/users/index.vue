<template>
  <section class="section">
    <div class="columns is-mobile">
      <Table
        class="column"
        :data="users"
        :columns="columns"
        control="User"/>
    </div>
  </section>
</template>
<script>
import Table from "~/components/Table";

export default {
  middleware: ['check-auth', 'auth', 'load-data'],
  components: {
    Table
  },
  data() {
    return {
      users: this.$store.getters.users,
      columns: [
        {
          field: 'id',
          label: 'ID',
        },
        {
          field: 'email',
          label: 'Email',
        },
        {
          field: 'name',
          label: 'Name',
        },
        {
          field: 'last_name',
          label: 'Last Name',
        },
        {
          field: 'role',
          label: 'Role',
        },
      ]
    }
  },
  methods:{
    removeUser(checkedRows, entity){
      const removedUsers = []
      checkedRows.map(user => {
        // this.$axios.$delete(
        //   `http://localhost:8080/api/v1/users/${user.id}`,
        //   {
        //     headers: {
        //       'Authorization': `${this.$store.getters.getToken}`
        //     }
        //   })
        removedUsers.push(user.id)
      })
      this.users = this.users.filter(user => !removedUsers.includes(user.id))
      this.$buefy.toast.open({
        message: entity + ' deleted!',
        type: 'is-danger'
      })
    }
  },
}
</script>

