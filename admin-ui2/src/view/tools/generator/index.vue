<template>
  <div>
    <Card shadow>
      <Row :gutter="32">
        <Col span="24" class="code-step">
          <Steps :current="current">
            <Step title="数据源配置" content="这里是该步骤的描述信息"></Step>
            <Step title="生成配置" content="这里是该步骤的描述信息"></Step>
          </Steps>
        </Col>
      </Row>
    </Card>
    <Card shadow>
      <!--
            <Row :gutter="32">
              <Col span="24" class="demo-tabs-style1" style="background: #e3e8ee;padding:16px;">
                <Tabs type="card">
                  <TabPane label="标签一">标签一的内容</TabPane>
                  <TabPane label="标签二">标签二的内容</TabPane>
                  <TabPane label="标签三">标签三的内容</TabPane>
                </Tabs>
              </Col>
            </Row>-->
      <Form :model="record" :label-width="80">

        <Card v-if="current === 0" :bordered="false">
          <Row :gutter="32">
            <Col span="6">
              <!--<FormItem label="Input">
                <Input v-model="formItem.input" placeholder="Enter something..."></Input>
              </FormItem>-->
              <FormItem label="类型">
                <Select v-model="record.dbType">
                  <Option value="MYSQL">MYSQL</Option>
                  <!--<Option value="ORACLE">ORACLE</Option>-->
                  <!--<Option value="shenzhen">Sydney</Option>-->
                </Select>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="地址">
                <Input v-model="record.dbHost" placeholder="Enter something..."></Input>
              </FormItem>
            </Col>
            <Col span="4">
              <FormItem label="端口">
                <!--<Input v-model="formItem.dbPort" placeholder="Enter something..."></Input>-->
                <InputNumber :max="100000" :min="1" v-model="record.dbPort"></InputNumber>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="用户">
                <Input v-model="record.dbUsername" placeholder="Enter something..."></Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="密码">
                <Input v-model="record.dbPassword" placeholder="Enter something..."></Input>
              </FormItem>
            </Col>

          </Row>
          <Row :gutter="32">
            <Col span="8">
              <FormItem label="数据库">
                <Input v-model="record.dbName" placeholder="Enter something..."></Input>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="表名">
                <Input v-model="record.dbTableName" placeholder="Enter something..."></Input>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="表主键">
                <Input v-model="record.dbTablePK" placeholder="Enter something..."></Input>
              </FormItem>
            </Col>
          </Row>
        </Card>

        <Card v-if="current === 1" :bordered="false">
          <Row :gutter="32">
            <Col span="12">
              <FormItem label="工具">
                <Select v-model="record.mapperPlugin">
                  <Option value="ZH">ZH</Option>
                  <Option value="TK">TK</Option>
                  <Option value="NONE">Google</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="生成包路径">
                <Input v-model="record.outputPackage" placeholder="Enter something..."></Input>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="作者">
                <Input v-model="record.commentAuthor" placeholder="Enter something..."></Input>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="实体基础类">
                <Select v-model="record.javaModelGeneratorRootClassMode">
                  <Option value="0">-</Option>
                  <Option value="1">BaseEntity</Option>
                  <Option value="2">BaseEntityWithStatus</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="Lombok">
                <i-switch v-model="record.mapperPluginLombok" size="large">
                  <span slot="open">On</span>
                  <span slot="close">Off</span>
                </i-switch>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="Swagger">
                <i-switch v-model="record.mapperPluginSwagger" size="large">
                  <span slot="open">On</span>
                  <span slot="close">Off</span>
                </i-switch>
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Row :gutter="32" style="margin-top: 15px">
          <FormItem>
            <Button v-if="current !== 0" type="warning" @click="next(-1)">Pre step</Button>
            <Button v-if="current !== 1" type="primary" @click="next(1)">Next step</Button>
            <Button v-if="current === 1" type="success" @click="submit">Generate</Button>
          </FormItem>
        </Row>
      </Form>
    </Card>
  </div>
</template>

<script>
import { generateCode, downloadZip } from '@/api/tools/generator'
const defaultObj = {
  dbType: 'MYSQL',
  dbHost: 'canalmgr-m.dbsit.sfcloud.local',
  dbPort: 3306,
  dbName: 'ch_upms',
  dbUsername: 'zhimin',
  dbPassword: 'admin123',
  dbTableName: 'bt_host',
  dbTablePK: 'id',
  mapperPlugin: 'ZH',
  javaModelGeneratorRootClassMode: '2',
  mapperPluginLombok: true,
  mapperPluginSwagger: true,
  outputPackage: 'com.ch',
  commentAuthor: 'zhimin'
}
export default {
  name: 'tools_gen_page',
  data () {
    return {
      current: 0,
      record: Object.assign({}, defaultObj),
      formItem: {
        input: '',
        select: '',
        radio: 'male',
        checkbox: [],
        switch: true,
        date: '',
        time: '',
        slider: [20, 50],
        textarea: ''
      }
    }
  },
  methods: {
    next (n) {
      if ((n > 0 && this.current >= 1) || (n < 0 && this.current === 0)) {
        return
      }
      this.current += n
    },
    submit () {
      generateCode(this.record).then(resp => {
        if (resp && resp.data && resp.data.success) {
          downloadZip({ filePath: resp.data.rows[0] })
        }
      })
    }
  }
}
</script>
<style>
  .code-step {
    margin: 10px;
  }
</style>
